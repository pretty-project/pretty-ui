
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.20
; Description:
; Version: v0.2.8
; Compatibility: x4.5.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.name-handler
    (:require [x.mid-locales.name-handler :as name-handler]
              [x.server-user.api          :as user]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-locales.name-handler
(def NAME-ORDERS        name-handler/NAME-ORDERS)
(def name->ordered-name name-handler/name->ordered-name)



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
