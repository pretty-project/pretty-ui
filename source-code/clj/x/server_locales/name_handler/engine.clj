
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.name-handler.engine
    (:require [x.server-user.api :as user]
              [x.mid-locales.name-handler.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-locales.name-handler.engine
(def NAME-ORDERS        engine/NAME-ORDERS)
(def name->ordered-name engine/name->ordered-name)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->name-order
  ; @param (map) request
  ;
  ; @return (keyword)
  ;  :normal, :reversed
  [request]
  (let [selected-language (user/request->user-settings-item request :selected-language)]
       (get NAME-ORDERS selected-language)))
