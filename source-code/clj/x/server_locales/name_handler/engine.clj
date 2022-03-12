
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.name-handler.engine
    (:require [x.mid-locales.name-handler.engine    :as name-handler.engine]
              [x.server-user.api                    :as user]
              [x.server-locales.name-handler.config :as name-handler.config]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-locales.name-handler.engine
(def name->ordered-name name-handler.engine/name->ordered-name)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->name-order
  ; @param (map) request
  ;
  ; @return (keyword)
  ;  :normal, :reversed
  [request]
  (let [selected-language (user/request->user-settings-item request :selected-language)]
       (get name-handler.config/NAME-ORDERS selected-language)))
