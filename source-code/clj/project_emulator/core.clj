
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns project-emulator.core
    (:require [shadow.cljs.devtools.server :as server]
              [shadow.cljs.devtools.api    :as shadow]
              [server-extensions.clients.api]
              [server-extensions.home.api]
              [server-extensions.products.api]
              [server-extensions.settings.api]
              [server-extensions.storage.api]
              [server-extensions.trader.api]
              [x.boot-loader]
              [project-emulator.server-router.api])
    (:gen-class))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn run-targeted-app!
  ; @param (map)
  ;  {:port (integer)}
  [{:keys [port] :as server-props}]
  (x.boot-loader/run-app! server-props)
  (println "project-emulator - Server started on port:" port))

(defn run-app!
  []
  (x.boot-loader/run-app!)
  (println "project-emulator - Server started"))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn -main
  ; @param (list of *) args
  ;  [(integer)(opt) port]
  ;
  ; @usage
  ;  (-main 3000)
  ;
  ; @usage
  ;  java -jar {{namespace}}.jar 3000
  [& [port :as args]]
  (if (some? port)
      (run-targeted-app! {:port port})
      (run-app!)))

(defn dev
  ; @param (map) options
  ;  {:port (integer)
  ;   :shadow-build (keyword)}
  ;
  ; @usage
  ;  (dev {:shadow-build :my-build})
  [{:keys [port shadow-build]}]
  (if (some? port)
      (-main port)
      (-main))
  (server/stop!)
  (server/start!)
  (shadow/watch shadow-build)
  (println "project-emulator - Development mode started"))
