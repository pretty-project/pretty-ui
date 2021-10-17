
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.17
; Description:
; Version: v0.6.6
; Compatibility: x4.3.3



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
  ;  => "Mentés"
  ;
  ; @example
  ;  (r dictionary/look-up :save! {:language-id :en})
  ;  => "Save"
  ;
  ; @example
  ;  (r dictionary/look-up :my-name-is {:replacements ["John"]})
  ;  => "Hi, my name is John"
  ;
  ; @return (string)
  [db [_ term-id {:keys [language-id replacements suffix]}]]
  (let [language-id       (or language-id (r locales/get-selected-language db))
        multilingual-term (r get-term db term-id)
        translated-term   (get multilingual-term language-id)
        suffixed-term     (str translated-term suffix)]
       (string/use-replacements suffixed-term replacements)))

(a/reg-sub :x.app-dictionary/look-up look-up)

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:language-id (keyword)(opt)
  ;    Default: (r locales/get-selected-language db)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @return (string)
  [term-id options]
  (a/subscribed [:x.app-dictionary/look-up term-id options]))

(defn translate
  ; @param (map) multilingual-item
  ;
  ; @example
  ;  (r dictionary/translate db {:en "Apple" :hu "Alma"})
  ;  => "Alma"
  ;
  ; @return (*)
  [db [_ multilingual-item]]
  (let [language-id (r locales/get-selected-language db)]
       (language-id multilingual-item)))

(a/reg-sub :x.app-dictionary/translate translate)

(defn translated
  ; @param (map) multilingual-item
  ;
  ; @return (*)
  [multilingual-item]
  (a/subscribed [:x.app-dictionary/translate multilingual-item]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) term-id
; @param (map) term
;
; @usage
;  [:x.app-dictionary/add-term! :my-term {:en "My term"}]
(a/reg-event-db :x.app-dictionary/add-term!  add-term!)

; @param (map) terms
;
; @usage
;  [:x.app-dictionary/add-terms! {:my-term {:en "My term"}}]
(a/reg-event-db :x.app-dictionary/add-terms! add-terms!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:x.app-dictionary/add-terms! BOOKS]})
