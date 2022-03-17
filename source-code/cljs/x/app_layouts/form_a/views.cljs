
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-layouts.form-a.views
    (:require [x.app-core.api     :as a]
              [x.app-elements.api :as elements]))



;; -- Input group components --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn input-group-label
  ; @param (keyword)(opt) label-id
  ; @param (map) label-props
  ;  {:color (keyword)(opt)
  ;   :content (metamorphic-content)}
  ;
  ; @usage
  ;  [layouts/input-group-label {...}]
  ;
  ; @usage
  ;  [layouts/input-group-label :my-label {...}]
  ([label-props]
   [input-group-label (a/id) label-props])

  ([label-id {:keys [content color]}]
   [elements/label label-id
                   {:font-size :m :font-weight :extra-bold :indent :both :layout :fit
                    :color color :content content}]))

(defn input-group-header
  ; @param (keyword)(opt) header-id
  ; @param (map) header-props
  ;  {:color (keyword)(opt)
  ;   :label (metamorphic-content)}
  ;
  ; @usage
  ;  [layouts/input-group-header {...}]
  ;
  ; @usage
  ;  [layouts/input-group-header :my-header {...}]
  ([header-props]
   [input-group-header (a/id) header-props])

  ([header-id {:keys [color label]}]
   [elements/row header-id
                 {:horizontal-align :left :content [input-group-label {:color color :content label}]}]))
