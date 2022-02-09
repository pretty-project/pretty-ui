
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.03.24
; Description:
; Version: v0.5.0
; Compatibility: x4.5.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-locales.language-handler
    (:require [mid-fruits.candy  :refer [param]]
              [mid-fruits.vector :as vector]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]
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
       (reduce #(conj %1 %2) [] app-languages)))

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

(defn get-multilingual-item
  ; @param (vector) item-path
  ;
  ; @usage
  ;  (r locales/get-multilingual-item db [:my-item])
  ;
  ; @return (*)
  [db [_ item-path]]
  (let [language-id        (r get-selected-language db)
        extended-item-path (conj item-path language-id)]
       (get-in db extended-item-path)))

(a/reg-sub :locales/get-multilingual-item get-multilingual-item)

(defn translate
  ; @param (map) n
  ;
  ; @usage
  ;  (r translate db {:en "Foo" :hu "Fú"})
  ;
  ; @return (string)
  [db [_ n]]
  (let [selected-language (r get-selected-language db)]
       (selected-language n)))

(a/reg-sub :locales/translate translate)



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-multilingual-item!
  ; @param (vector) item-path
  ; @param (*) item
  ;
  ; @return (map)
  [db [_ item-path item]]
  (let [language-id (r get-selected-language db)
        extended-item-path (conj item-path language-id)]
       (r db/set-item! db extended-item-path item)))

(a/reg-event-db :locales/set-multilingual-item! set-multilingual-item!)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn translated
  ; @param (map) n
  ;
  ; @usage
  ;  (translated {:en "Foo" :hu "Fú"})
  [n]
 @(a/subscribe [:locales/translate n]))
