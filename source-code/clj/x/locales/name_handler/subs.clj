
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.locales.name-handler.subs
    (:require [mid-fruits.string              :as string]
              [re-frame.api                   :as r :refer [r]]
              [x.locales.name-handler.config  :as name-handler.config]
              [x.locales.name-handler.helpers :as name-handler.helpers]))



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

(defn get-ordered-initials
  ; @param (string) first-name
  ; @param (string) last-name
  ; @param (keyword) locale-id
  ;
  ; @example
  ;  (r get-ordered-initials db "First" "Last" :en)
  ;  =>
  ;  "FL"
  ;
  ; @return (string)
  [db [_ first-name last-name locale-id]]
  (let [name-order (r get-name-order db locale-id)]
       (name-handler.helpers/name->ordered-initials first-name last-name name-order)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.locales/get-name-order :en]
(r/reg-sub :x.locales/get-name-order get-name-order)

; @usage
;  [:x.locales/get-ordered-name "First" "Last" :en]
(r/reg-sub :x.locales/get-ordered-name get-ordered-name)

; @usage
;  [:x.locales/get-ordered-initials "First" "Last" :en]
(r/reg-sub :x.locales/get-ordered-initials get-ordered-initials)
