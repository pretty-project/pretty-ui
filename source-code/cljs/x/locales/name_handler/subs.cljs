
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.locales.name-handler.subs
    (:require [re-frame.api                    :as r :refer [r]]
              [x.locales.language-handler.subs :as language-handler.subs]
              [x.locales.name-handler.config   :as name-handler.config]
              [x.locales.name-handler.helpers  :as name-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-name-order
  ; @usage
  ;  (r get-name-order db)
  ;
  ; @return (keyword)
  ;  :normal, :reversed
  [db _]
  (let [selected-language (r language-handler.subs/get-selected-language db)]
       (get name-handler.config/NAME-ORDERS selected-language :normal)))

(defn get-ordered-name
  ; @param (string) first-name
  ; @param (string) last-name
  ;
  ; @usage
  ;  (r get-ordered-name db "First" "Last")
  ;
  ; @return (string)
  [db [_ first-name last-name]]
  (let [selected-language (r language-handler.subs/get-selected-language db)]
       (name-handler.helpers/name->ordered-name first-name last-name selected-language)))

(defn get-ordered-initials
  ; @param (string) first-name
  ; @param (string) last-name
  ;
  ; @usage
  ;  (r get-ordered-initials db "First" "Last")
  ;
  ; @return (string)
  [db [_ first-name last-name]]
  (let [selected-language (r language-handler.subs/get-selected-language db)]
       (name-handler.helpers/name->ordered-initials first-name last-name selected-language)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.locales/get-name-order]
(r/reg-sub :x.locales/get-name-order get-name-order)

; @usage
;  [:x.locales/get-ordered-name "First" "Last"]
(r/reg-sub :x.locales/get-ordered-name get-ordered-name)

; @usage
;  [:x.locales/get-ordered-initials "First" "Last"]
(r/reg-sub :x.locales/get-ordered-initials get-ordered-initials)
