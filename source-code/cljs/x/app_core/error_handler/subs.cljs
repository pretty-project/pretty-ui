
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.error-handler.subs
    (:require [candy.api                       :refer [param return]]
              [x.app-core.debug-handler.subs   :as debug-handler.subs]
              [x.app-core.error-handler.config :as error-handler.config]
              [x.app-core.event-handler        :as event-handler :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-developer-error-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-id
  ; @param (map) error-props
  ;  {:error (string)(opt)}
  ;
  ; @return (string)
  [_ [_ _ {:keys [error]}]]
  (if error (str error)
            (str error-handler.config/DEFAULT-APPLICATION-ERROR)))

(defn get-error-message
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) error-id
  ; @param (map) error-props
  ;
  ; @return (string)
  [db [_ error-id error-props]]
  (if (r debug-handler.subs/debug-mode-detected? db)
      (r get-developer-error-message             db error-id error-props)
      (return error-handler.config/DEFAULT-APPLICATION-ERROR)))
