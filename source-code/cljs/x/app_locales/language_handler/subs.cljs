
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.language-handler.subs
    (:require [mid-fruits.vector :as vector]
              [re-frame.api      :as r :refer [r]]
              [x.app-core.api    :as core]
              [x.app-user.api    :as user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-languages
  ; @usage
  ;  (r get-app-languages db)
  ;
  ; @return (keywords in vector)
  [db _]
  (let [app-languages (r core/get-app-config-item db :app-languages)]
       (reduce conj [] app-languages)))

(defn app-multilingual?
  ; @usage
  ;  (r app-multilingual? db)
  ;
  ; @return (boolean)
  [db _]
  (let [app-languages (r get-app-languages db)]
       (vector/min? app-languages 2)))

(defn get-selected-language
  ; @usage
  ;  (r get-selected-language db)
  ;
  ; @return (keyword)
  [db _]
  (r user/get-user-settings-item db :selected-language))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:locales/get-app-languages]
(r/reg-sub :locales/get-app-languages get-app-languages)

; @usage
;  [:locales/app-multilingual?]
(r/reg-sub :locales/app-multilingual? app-multilingual?)

; @usage
;  [:locales/get-selected-language]
(r/reg-sub :locales/get-selected-language get-selected-language)
