
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.config-handler.checks)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn check-app-config
  ; @param (map) app-config
  [app-config]
  (cond (or (->  app-config :app-domain string? not)
            (->  app-config :app-domain empty?))
        (throw (Exception. "(x.app-config.edn) :app-domain must be a nonempty string!"))
        (or (->  app-config :app-home string? not)
            (->  app-config :app-home empty?))
        (throw (Exception. "(x.app-config.edn) :app-home must be a nonempty string!"))
        (or (->  app-config :app-languages vector? not)
            (->  app-config :app-languages empty?)
            (->> app-config :app-languages (some #(-> % keyword? not))))
        (throw (Exception. "(x.app-config.edn) :app-languages must be a nonempty vector with keyword items!"))
        (or (->  app-config :app-locale keyword? not))
        (throw (Exception. "(x.app-config.edn) :app-locale must be a keyword!"))
        (or (->  app-config :app-themes vector? not)
            (->  app-config :app-themes empty?)
            (->> app-config :app-themes (some #(-> % map? not))))
        (throw (Exception. "(x.app-config.edn) :app-themes must be a nonempty vector with map items!"))
        (or (->  app-config :app-title string? not)
            (->  app-config :app-title empty?))
        (throw (Exception. "(x.app-config.edn) :app-title must be a nonempty string!"))
        (or (->  app-config :author string? not)
            (->  app-config :author empty?))
        (throw (Exception. "(x.app-config.edn) :author must be a nonempty string!"))))

(defn check-server-config
  ; @param (map) server-config
  [server-config]
  (cond (or (->  server-config :database-host string? not)
            (->  server-config :database-host empty?))
        (throw (Exception. "(x.server-config.edn) :database-host must be a nonempty string!"))
        (or (->  server-config :database-name string? not)
            (->  server-config :database-name empty?))
        (throw (Exception. "(x.server-config.edn) :database-name must be a nonempty string!"))
        (or (->  server-config :database-port integer? not))
        (throw (Exception. "(x.server-config.edn) :database-port must be an integer!"))
        (or (->  server-config :resources vector? not)
            (->  server-config :resources empty?)
            (->> server-config :resources (some #(-> % map? not))))
        (throw (Exception. "(x.server-config.edn) :resources must be a nonempty vector with map items!"))
        (or (->  server-config :max-upload-size integer? not))
        (throw (Exception. "(x.server-config.edn) :max-upload-size must be an integer!"))
        (or (->  server-config :storage-capacity integer? not))
        (throw (Exception. "(x.server-config.edn) :storage-capacity must be an integer!"))
        (or (->  server-config :default-port integer? not))
        (throw (Exception. "(x.server-config.edn) :default-port must be an integer!"))
        (or (->  server-config :max-body integer? not))
        (throw (Exception. "(x.server-config.edn) :max-body must be an integer!"))))
