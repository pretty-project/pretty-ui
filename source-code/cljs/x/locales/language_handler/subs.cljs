
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.locales.language-handler.subs
    (:require [mid-fruits.vector :as vector]
              [re-frame.api      :as r :refer [r]]
              [x.core.api        :as x.core]
              [x.user.api        :as x.user]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-languages
  ; @usage
  ;  (r get-app-languages db)
  ;
  ; @return (keywords in vector)
  [db _]
  (let [app-languages (r x.core/get-app-config-item db :app-languages)]
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
  (r x.user/get-user-settings-item db :selected-language))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:x.locales/get-app-languages]
(r/reg-sub :x.locales/get-app-languages get-app-languages)

; @usage
;  [:x.locales/app-multilingual?]
(r/reg-sub :x.locales/app-multilingual? app-multilingual?)

; @usage
;  [:x.locales/get-selected-language]
(r/reg-sub :x.locales/get-selected-language get-selected-language)
