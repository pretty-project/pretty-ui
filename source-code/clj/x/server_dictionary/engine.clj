
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.17
; Description:
; Version: v0.1.8
; Compatibility: x4.4.0



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.engine
    (:require [mid-fruits.string         :as string]
              [x.mid-dictionary.engine   :as engine]
              [x.server-core.api         :as a :refer [r]]
              [x.server-dictionary.books :as books]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-dictionary.engine
(def get-term     engine/get-term)
(def term-exists? engine/term-exists?)
(def add-term!    engine/add-term!)
(def add-terms!   engine/add-terms!)

; x.server-dictionary.books
(def BOOKS books/BOOKS)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn look-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:language-id (keyword)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (r dictionary/look-up :save! {:language-id :en})
  ;  =>
  ;  "Save"
  ;
  ; @example
  ;  (r dictionary/look-up :my-name-is {:language-id :en :replacements ["John"]})
  ;  =>
  ;  "Hi, my name is John"
  ;
  ; @return (string)
  [db [_ term-id {:keys [language-id replacements suffix]}]]
  (let [multilingual-term (r get-term db term-id)
        translated-term   (get multilingual-term language-id)
        suffixed-term     (str translated-term suffix)]
       (string/use-replacements suffixed-term replacements)))

(a/reg-sub :x.server-dictionary/look-up look-up)

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:language-id (keyword)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @return (string)
  [term-id options]
  (a/subscribed [:x.app-dictionary/look-up term-id options]))



;; -- DB events ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @param (keyword) term-id
; @param (map) term
;
; @usage
;  [:x.server-dictionary/add-term! :my-term {:en "My term"}]
(a/reg-event-db :x.server-dictionary/add-term!  add-term!)

; @param (map) terms
;
; @usage
;  [:x.server-dictionary/add-terms! {:my-term {:en "My term"}}]
(a/reg-event-db :x.server-dictionary/add-terms! add-terms!)



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles
  ::lifecycles
  {:on-app-init [:x.server-dictionary/add-terms! BOOKS]})
