
(ns backend
    (:require [backend.router.api]
              [extensions.clients.api]
              [extensions.home-screen.api]
              [extensions.settings.api]
              [extensions.storage.api]
              [extensions.trader.api]
              [sente.api]
              [x.boot-loader.api]
              [shadow.cljs.devtools.server :as server]
              [shadow.cljs.devtools.api    :as shadow]
              ; DEBUG
              [playground.api])
    (:gen-class))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn start-targeted-server!
  ; @param (map)
  ;  {:port (integer)}
  [{:keys [port] :as server-props}]
  (println "project-emulator - Starting server on port:" port "...")
  (x.boot-loader.api/start-server! server-props))

(defn start-server!
  []
  (println "project-emulator - Starting server ...")
  (x.boot-loader.api/start-server!))



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
  ([port] (start-targeted-server! {:port port})))

(defn dev
  ; @param (map) options
  ;  {:port (integer)
  ;   :shadow-build (keyword)}
  ;
  ; @usage
  ;  (dev {:shadow-build :my-build})
  [{:keys [port shadow-build]}]
  (if port (-main port)
           (-main))
  (server/stop!)
  (server/start!)
  (shadow/watch shadow-build)
  (println "project-emulator - Development mode started"))
