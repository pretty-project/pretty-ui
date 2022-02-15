
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.17
; Description:
; Version: v0.2.8
; Compatibility: x4.6.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-dictionary.engine
    (:require [mid-fruits.candy          :refer [param return]]
              [mid-fruits.string         :as string]
              [server-fruits.io          :as io]
              [x.mid-dictionary.engine   :as engine]
              [x.server-core.api         :as a :refer [r]]
              [x.server-db.api           :as db]
              [x.server-dictionary.books :as books]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (string)
(def PROJECT-DICTIONARY-FILEPATH "monoset-environment/x.project-dictionary.edn")



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
  ; @param (map) options
  ;  {:language-id (keyword)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (r dictionary/look-up :my-term! {:language-id :en})
  ;  =>
  ;  "My term"
  ;
  ; @example
  ;  (r dictionary/look-up :my-name-is-n {:language-id :en :replacements ["John"]})
  ;  =>
  ;  "My name is John"
  ;
  ; @return (string)
  [db [_ term-id {:keys [language-id replacements suffix]}]]
  (let [translated-term (r get-term db term-id language-id)
        suffixed-term   (str translated-term suffix)]
       (if replacements (string/use-replacements suffixed-term replacements)
                        (return                  suffixed-term))))

; @usage
;  [:dictionary/look-up :my-term {:language-id :en}]
(a/reg-sub :dictionary/look-up look-up)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn looked-up
  ; @param (keyword) term-id
  ; @param (map) options
  ;  {:language-id (keyword)
  ;   :replacements (vector)(opt)
  ;    XXX#4509
  ;   :suffix (string)(opt)}
  ;
  ; @example
  ;  (dictionary/looked-up :my-term {:language-id :en})
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
(a/reg-event-db :dictionary/add-term! add-term!)

; @param (map) terms
;
; @usage
;  [:dictionary/add-terms! {:my-term {:en "My term"}}]
(a/reg-event-db :dictionary/add-terms! add-terms!)



;; -- Side-effect events ------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- import-project-dictionary!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (a/dispatch [:dictionary/add-terms! (io/read-edn-file PROJECT-DICTIONARY-FILEPATH)]))

(a/reg-fx :dictionary/import-project-dictionary! import-project-dictionary!)



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :dictionary/initialize-dictionary!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [_ _]
      {:fx       [:dictionary/import-project-dictionary!]
       :dispatch [:dictionary/add-terms! BOOKS]}))



;; -- Lifecycle events --------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-lifecycles!
  ::lifecycles
  {:on-server-init [:dictionary/initialize-dictionary!]})

(a/reg-transfer!
  :dictionary/transfer-project-dictionary!
  {:data-f      (fn [_] (io/read-edn-file PROJECT-DICTIONARY-FILEPATH))
   :target-path (db/path :dictionary/terms)})
