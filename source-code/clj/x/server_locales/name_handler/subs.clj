
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-locales.name-handler.subs
    (:require [mid-fruits.string                     :as string]
              [re-frame.api                          :as r :refer [r]]
              [x.server-locales.name-handler.config  :as name-handler.config]
              [x.server-locales.name-handler.helpers :as name-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-name-order
  ; @param (keyword) locale-id
  ;
  ; @example
  ;  (r get-name-order db :en)
  ;  =>
  ;  :normal
  ;
  ; @return (keyword)
  ;  :normal, :reversed
  [db [_ locale-id]]
  (get name-handler.config/NAME-ORDERS locale-id :normal))

(defn get-ordered-name
  ; @param (string) first-name
  ; @param (string) last-name
  ; @param (keyword) locale-id
  ;
  ; @example
  ;  (r get-ordered-name db "First" "Last" :en)
  ;  =>
  ;  "First Last"
  ;
  ; @return (string)
  [db [_ first-name last-name locale-id]]
  (let [name-order (r get-name-order db locale-id)]
       (name-handler.helpers/name->ordered-name first-name last-name name-order)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:locales/get-name-order :en]
(r/reg-sub :locales/get-name-order get-name-order)

; @usage
;  [:locales/get-ordered-name "First" "Last" :en]
(r/reg-sub :locales/get-ordered-name get-ordered-name)
