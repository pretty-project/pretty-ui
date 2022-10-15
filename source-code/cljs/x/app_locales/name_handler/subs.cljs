
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.name-handler.subs
    (:require [re-frame.api                        :as r :refer [r]]
              [x.app-locales.language-handler.subs :as language-handler.subs]
              [x.app-locales.name-handler.config   :as name-handler.config]
              [x.app-locales.name-handler.helpers  :as name-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-name-order
  ; @usage
  ;  (r locales/get-name-order db)
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
  ;  (r locales/get-ordered-name db "First" "Last")
  ;
  ; @return (string)
  [db [_ first-name last-name]]
  (let [selected-language (r language-handler.subs/get-selected-language db)]
       (name-handler.helpers/name->ordered-name first-name last-name selected-language)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:locales/get-name-order]
(r/reg-sub :locales/get-name-order get-name-order)

; @usage
;  [:locales/get-ordered-name "First" "Last"]
(r/reg-sub :locales/get-ordered-name get-ordered-name)
