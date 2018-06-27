(ns file-filterer.core
    (:require
      [clojure.pprint :refer [pprint]]
      [puget.printer :refer [cprint]]
      [clojure.data.json :as json]
      [template-generator.executors.search.terms :refer :all])
  (:gen-class))

(defn go []
    (let
       [config-file "config.json"
        config (json/read-str (slurp config-file) :key-fn keyword)]

       (do

         (println (str "configuration read from " config-file ":"))
         (cprint config)

         (doseq [term-file-name (:term-file-names config)
                     input-datasets (:input-dataset-names config)]
            (keywords-filter
                (str (:term-files-path config) "/" term-file-name)
                (str (:input-datasets-path config) "/" (:name input-datasets))
                (:nickname input-datasets)
                (str (:output-path config) "/" (:nickname input-datasets) "." (:term-file-name config))))

       (shutdown-agents))))

(defn -main [& args]
  (go))
