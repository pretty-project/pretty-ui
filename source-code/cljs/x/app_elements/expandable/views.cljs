
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.expandable.views
    (:require [mid-fruits.random                    :as random]
              [x.app-components.api                 :as components]
              [x.app-elements.expandable.helpers    :as expandable.helpers]
              [x.app-elements.expandable.prototypes :as expandable.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- expandable-expand-button
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;  {:expanded? (boolean)}
  [expandable-id {:keys [expanded?]}]
  (if (expandable.helpers/expanded? expandable-id)
      [:i.x-expandable--expand-icon {:data-icon-family :material-icons-filled} :expand_less]
      [:i.x-expandable--expand-icon {:data-icon-family :material-icons-filled} :expand_more]))

(defn- expandable-icon
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;  {:icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)}
  [_ {:keys [icon icon-family]}]
  (if icon [:i.x-expandable--icon {:data-icon-family icon-family} icon]))

(defn- expandable-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;  {:label (metamorphic-content)(opt)}
  [_ {:keys [label]}]
  (if label [:div.x-expandable--label (components/content label)]))

(defn- expandable-header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  [:button.x-expandable--header (expandable.helpers/expandable-header-attributes expandable-id expandable-props)
                                [expandable-icon                                 expandable-id expandable-props]
                                [expandable-label                                expandable-id expandable-props]
                                [expandable-expand-button                        expandable-id expandable-props]])

(defn- expandable-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  ;  {:content (metamorphic-content)(opt)}
  [expandable-id {:keys [content]}]
  (if (expandable.helpers/expanded? expandable-id)
      [:div.x-expandable--body [components/content expandable-id content]]))

(defn expandable
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) expandable-id
  ; @param (map) expandable-props
  [expandable-id expandable-props]
  [:div.x-expandable (expandable.helpers/expandable-attributes expandable-id expandable-props)
                     [expandable-header                        expandable-id expandable-props]
                     [expandable-body                          expandable-id expandable-props]])

(defn element
  ; @param (keyword)(opt) expandable-id
  ; @param (map) expandable-props
  ;  {:content (metamorphic-content)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :expanded? (boolean)(opt)
  ;    Default: true
  ;   :icon (keyword)(opt)
  ;   :icon-family (keyword)(opt)
  ;    :material-icons-filled, :material-icons-outlined
  ;    Default: :material-icons-filled
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :label (metamorphic-content)(opt)}
  ;
  ; @usage
  ;  [elements/expandable {...}]
  ;
  ; @usage
  ;  [elements/expandable :my-expandable {...}]
  ([expandable-props]
   [element (random/generate-keyword) expandable-props])

  ([expandable-id expandable-props]
   (let [expandable-props (expandable.prototypes/expandable-props-prototype expandable-props)]
        [expandable expandable-id expandable-props])))