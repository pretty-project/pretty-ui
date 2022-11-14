
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
  ;  {}
  [{:keys [app-domain app-home app-languages app-locale app-themes app-title author]}]
  (cond (or (-> app-domain string? not)
            (-> app-domain empty?))
        (throw (Exception. "(x.app-config.edn) :app-domain must be a nonempty string!"))
        (or (-> app-home string? not)
            (-> app-home empty?))
        (throw (Exception. "(x.app-config.edn) :app-home must be a nonempty string!"))
        (or (-> app-languages vector? not)
            (-> app-languages empty?)
            (some #(-> % keyword? not) app-languages))
        (throw (Exception. "(x.app-config.edn) :app-languages must be a nonempty vector with keyword items!"))
        (-> app-locale keyword? not)
        (throw (Exception. "(x.app-config.edn) :app-locale must be a keyword!"))
        (or (-> app-themes vector? not)
            (-> app-themes empty?)
            (some #(-> % map? not) app-themes))
        (throw (Exception. "(x.app-config.edn) :app-themes must be a nonempty vector with map items!"))
        (or (-> app-title string? not)
            (-> app-title empty?))
        (throw (Exception. "(x.app-config.edn) :app-title must be a nonempty string!"))
        (or (-> author string? not)
            (-> author empty?))
        (throw (Exception. "(x.app-config.edn) :author must be a nonempty string!"))))

(defn check-server-config
  ; @param (map) server-config
  ;  {}
  [{:keys [database-host database-name database-port resources max-upload-size storage-capacity default-port max-body]}]
  (cond (or (-> database-host string? not)
            (-> database-host empty?))
        (throw (Exception. "(x.server-config.edn) :database-host must be a nonempty string!"))
        (or (-> database-name string? not)
            (-> database-name empty?))
        (throw (Exception. "(x.server-config.edn) :database-name must be a nonempty string!"))
        (-> database-port integer? not)
        (throw (Exception. "(x.server-config.edn) :database-port must be an integer!"))
        (or (-> resources vector? not)
            (-> resources empty?)
            (some #(-> % map? not) resources))
        (throw (Exception. "(x.server-config.edn) :resources must be a nonempty vector with map items!"))
        (-> max-upload-size integer? not)
        (throw (Exception. "(x.server-config.edn) :max-upload-size must be an integer!"))
        (-> storage-capacity integer? not)
        (throw (Exception. "(x.server-config.edn) :storage-capacity must be an integer!"))
        (-> default-port integer? not)
        (throw (Exception. "(x.server-config.edn) :default-port must be an integer!"))
        (-> max-body integer? not)
        (throw (Exception. "(x.server-config.edn) :max-body must be an integer!"))))
