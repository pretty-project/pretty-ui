
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.10.17
; Description:
; Version: v0.8.8
; Compatibility: x4.6.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-dictionary.engine
    (:require [mid-fruits.candy        :refer [param return]]
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
  ;  {:prefix (string)(opt)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (r dictionary/look-up :my-term)
  ;  =>
  ;  "My term"
  ;
  ; @example
  ;  (r dictionary/look-up :my-name-is-n {:replacements ["John"]})
  ;  =>
  ;  "My name is John"
  ;
  ; @return (string)
  [db [_ term-id {:keys [prefix replacements suffix]}]]
  (let [language-id     (r locales/get-selected-language db)
        translated-term (r get-term db term-id language-id)]
       (if replacements (string/use-replacements (str prefix translated-term suffix) replacements)
                        (str prefix translated-term suffix))))

; @usage
;  [:dictionary/look-up! :my-term]
(a/reg-sub :dictionary/look-up look-up)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map)(opt) options
  ;  {:replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (dictionary/looked-up :my-term)
  ;  =>
  ;  "My term"
  ;
  ; @return (string)
  [term-id options]
 @(a/subscribe [:dictionary/look-up term-id options]))



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

(a/reg-lifecycles!
  ::lifecycles
  {:on-app-init [:dictionary/add-terms! BOOKS]})
