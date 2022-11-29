
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.item-sorter.views
    (:require [dnd-kit.api                    :as dnd-kit]
              [elements.api                   :as elements]
              [engines.text-sorter.helpers    :as helpers]
              [engines.text-sorter.prototypes :as prototypes]
              [engines.text-sorter.state      :as state]
              [random.api                     :as random]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- item-sorter-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sorter-id
  ; @param (map) sorter-props
  [sorter-id sorter-props]
  (let [dnd-kit-props (prototypes/dnd-kit-props-prototype sorter-id sorter-props)]
       [dnd-kit/body sorter-id dnd-kit-props]))

(defn- item-sorter
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) sorter-id
  ; @param (map) sorter-props
  ;  {:disabled? (boolean)(opt)
  ;   :indent (map)(opt)}
  [sorter-id {:keys [disabled? indent] :as sorter-props}]
  [elements/blank sorter-id
                  {:content   [item-sorter-body sorter-id sorter-props]
                   :disabled? disabled?
                   :indent    indent}])

(defn body
  ; @param (keyword)(opt) sorter-id
  ; @param (map) sorter-props
  ;  {:disabled? (boolean)(opt)
  ;    Default: false
  ;   :indent (map)(opt)
  ;    A tulajdonság leírását a elements.api/blank dokumentációjában találod!
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :value-path (vector)}
  ;
  ; @usage
  ;  [body {...}]
  ;
  ; @usage
  ;  [body :my-sorter {...}]
  ([sorter-props]
   [body (random/generate-keyword) sorter-props])

  ([sorter-id sorter-props]
   (let [sorter-props (prototypes/sorter-props-prototype sorter-id sorter-props)]
        [item-sorter sorter-id sorter-props])))
