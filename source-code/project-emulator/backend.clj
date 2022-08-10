
(ns backend
    (:require [backend.router.api]
              [sente.api]
              [x.boot-loader.api]
              [shadow.cljs.devtools.server :as server]
              [shadow.cljs.devtools.api    :as shadow])
    (:gen-class))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-server!
  ; @param (map) server-props
  ;  {:port (integer)}
  [{:keys [port] :as server-props}]
  (println "project-emulator - Starting server on port: " (or port "default") " ...")
  (x.boot-loader.api/start-server! server-props))



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
  ([]     (start-server!))
  ([port] (start-server! {:port port})))

(defn dev
  ; @param (map) options
  ;  {:port (integer)
  ;   :shadow-build (keyword)}
  ;
  ; @usage
  ;  (dev {:shadow-build :my-build})
  [{:keys [port shadow-build]}]
  (start-server! {:port port :dev-mode? true})
  (server/stop!)
  (server/start!)
  (shadow/watch shadow-build)
  (println "project-emulator - Development mode started"))
