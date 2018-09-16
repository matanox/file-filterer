(ns file-filterer.core
    (:require
      [clojure.pprint :refer [pprint]]
      [puget.printer :refer [cprint]]
      [clojure.data.json :as json]
      [scjsv.core :as json-schema]
      [temp-text-box.executors.search.terms :refer :all])
  (:gen-class))

(def config-file "config.json")
(def config-schema "config.schema.json")

(defn config-schema-validation-errors []
    (let [json-validator (json-schema/json-validator (-> config-schema slurp json/read-str))]
       (json-validator (-> config-file slurp))))

(defn go []
    (let
       [config (json/read-str (slurp config-file) :key-fn keyword)]
       (do
         (println (str "configuration read from " config-file ":"))
         (cprint config)

         (doseq [term-file-name (:term-file-names config)
                     input-datasets (:input-dataset-names config)]
            (keywords-filter
                (str (:term-files-path config) "/" term-file-name)
                (:min-occurrences config)
                (str (:input-datasets-path config) "/" (:name input-datasets))
                (:nickname input-datasets)
                (str (:output-path config) "/" (:nickname input-datasets) "." term-file-name)))

       (shutdown-agents))))

(defn -main [& args]
  (if-not (.exists (clojure.java.io/as-file config-file))
     (println (str "No " config-file " file found; a config.json file should specify what to do, please see file example-config.json, as an example."))
     (if-let [config-errors (config-schema-validation-errors)]
        (do
          (println (str config-file " has errors:"))
          (cprint (map #(dissoc % :domain :schema :keyword :level) config-errors)))
        (go))))
