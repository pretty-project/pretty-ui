
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.language-handler.subs
    (:require [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-user.api    :as user]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-app-languages
  ; @usage
  ;  (r locales/get-app-languages db)
  ;
  ; @return (keywords in vector)
  [db _]
  (let [app-languages (r a/get-app-config-item db :app-languages)]
       (reduce conj [] app-languages)))

(defn app-multilingual?
  ; @usage
  ;  (r locales/app-multilingual? db)
  ;
  ; @return (boolean)
  [db _]
  (let [app-languages (r get-app-languages db)]
       (vector/min? app-languages 2)))

(defn get-selected-language
  ; @usage
  ;  (r locales/get-selected-language db)
  ;
  ; @return (keyword)
  [db _]
  (r user/get-user-settings-item db :selected-language))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:locales/get-app-languages]
(a/reg-sub :locales/get-app-languages get-app-languages)

; @usage
;  [:locales/app-multilingual?]
(a/reg-sub :locales/app-multilingual? app-multilingual?)

; @usage
;  [:locales/get-selected-language]
(a/reg-sub :locales/get-selected-language get-selected-language)
