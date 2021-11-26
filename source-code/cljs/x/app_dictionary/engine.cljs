
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.17
; Description:
; Version: v0.8.0
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-dictionary.engine
    (:require [mid-fruits.candy        :refer [param]]
              [mid-fruits.map          :as map]
              [mid-fruits.string       :as string]
              [x.app-core.api          :as a :refer [r]]
              [x.app-dictionary.books  :as books]
              [x.app-locales.api       :as locales]
              [x.mid-dictionary.engine :as engine]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.app-dictionary.books
(def BOOKS books/BOOKS)

; x.mid-dictionary.engine
(def get-term     engine/get-term)
(def term-exists? engine/term-exists?)
(def add-term!    engine/add-term!)
(def add-terms!   engine/add-terms!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn look-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:language-id (keyword)(opt)
  ;    Default: (r locales/get-selected-language db)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (r dictionary/look-up :save!)
  ;  =>
  ;  "Mentés"
  ;
  ; @example
  ;  (r dictionary/look-up :save! {:language-id :en})
  ;  =>
  ;  "Save"
  ;
  ; @example
  ;  (r dictionary/look-up :my-name-is {:replacements ["John"]})
  ;  =>
  ;  "Hi, my name is John"
  ;
  ; @return (string)
  [db [_ term-id {:keys [language-id replacements suffix]}]]
  (let [language-id       (or language-id (r locales/get-selected-language db))
        multilingual-term (r get-term db term-id)
        translated-term   (get multilingual-term language-id)
        suffixed-term     (str translated-term suffix)]
       (string/use-replacements suffixed-term replacements)))

; @usage
;  [:dictionary/look-up! :my-term]
(a/reg-sub :dictionary/look-up look-up)

(defn translate
  ; @param (map) multilingual-item
  ;
  ; @example
  ;  (r dictionary/translate db {:en "Apple" :hu "Alma"})
  ;  =>
  ;  "Alma"
  ;
  ; @return (*)
  [db [_ multilingual-item]]
  (let [language-id (r locales/get-selected-language db)]
       (language-id multilingual-item)))

; @usage
;  [:dictionary/translate {:en "Apple" :hu "Alma"}]
(a/reg-sub :dictionary/translate translate)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:language-id (keyword)(opt)
  ;    Default: (r locales/get-selected-language db)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (dictionary/looked-up :save!)
  ;  =>
  ;  "Mentés"
  ;
  ; @return (string)
  [term-id options]
  (a/subscribed [:dictionary/look-up term-id options]))

(defn translated
  ; @param (map) multilingual-item
  ;
  ; @example
  ;  (dictionary/translated {:en "Apple" :hu "Alma"})
  ;  =>
  ;  "Alma"
  ;
  ; @return (*)
  [multilingual-item]
  (a/subscribed [:dictionary/translate multilingual-item]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) term-id
; @param (map) term
;
; @usage
;  [:dictionary/add-term! :my-term {:en "My term"}]
(a/reg-event-db :dictionary/add-term!  add-term!)

; @param (map) terms
;
; @usage
;  [:dictionary/add-terms! {:my-term {:en "My term"}}]
(a/reg-event-db :dictionary/add-terms! add-terms!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:dictionary/add-terms! BOOKS]})
