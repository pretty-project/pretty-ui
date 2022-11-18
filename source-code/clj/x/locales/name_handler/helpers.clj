
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.locales.name-handler.helpers
    (:require [mid.x.locales.name-handler.helpers :as name-handler.helpers]
              [x.user.api                         :as x.user]
              [x.locales.name-handler.config      :as name-handler.config]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; mid.x.locales.name-handler.helpers
(def name->ordered-name     name-handler.helpers/name->ordered-name)
(def name->ordered-initials name-handler.helpers/name->ordered-initials)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn request->name-order
  ; @param (map) request
  ;
  ; @return (keyword)
  ;  :normal, :reversed
  [request]
  (let [selected-language (x.user/request->user-settings-item request :selected-language)]
       (get name-handler.config/NAME-ORDERS selected-language)))

(defn request->ordered-user-name
  ; @param (map) request
  ;
  ; @usage
  ;  (request->ordered-user-name {...})
  ;
  ; @return (string)
  [request]
  (let [first-name        (x.user/request->user-profile-item  request :first-name)
        last-name         (x.user/request->user-profile-item  request :last-name)
        selected-language (x.user/request->user-settings-item request :selected-language)]
       (name->ordered-name first-name last-name selected-language)))