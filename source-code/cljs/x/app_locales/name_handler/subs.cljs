
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2022.02.22
; Description:
; Version: v0.4.4
; Compatibility: x4.6.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.name-handler.subs
    (:require [mid-fruits.string :as string]
              [x.app-core.api    :as a :refer [r]]
              [x.app-locales.language-handler.api :as language-handler]
              [x.app-locales.name-handler.engine  :as engine]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-name-order
  ; @usage
  ;  (r locales/get-name-order db)
  ;
  ; @return (keyword)
  ;  :normal, :reversed
  [db _]
  (let [selected-language (r language-handler/get-selected-language db)]
       (get engine/NAME-ORDERS selected-language :normal)))

(defn get-ordered-name
  ; @param (string) first-name
  ; @param (string) last-name
  ;
  ; @usage
  ;  (r locales/get-ordered-name db "First" "Last")
  ;
  ; @return (string)
  [db [_ first-name last-name]]
  (let [name-order (r get-name-order db)]
       (string/trim (case name-order :reversed (str last-name  " " first-name)
                                               (str first-name " " last-name)))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:locales/get-name-order]
(a/reg-sub :locales/get-name-order get-name-order)

; @usage
;  [:locales/get-ordered-name "First" "Last"]
(a/reg-sub :locales/get-ordered-name get-ordered-name)
