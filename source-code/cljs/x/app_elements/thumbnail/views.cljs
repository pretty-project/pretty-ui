
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.thumbnail.views
    (:require [mid-fruits.css                      :as css]
              [mid-fruits.random                   :as random]
              [x.app-elements.label.views          :as label.views]
              [x.app-elements.thumbnail.helpers    :as thumbnail.helpers]
              [x.app-elements.thumbnail.prototypes :as thumbnail.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- thumbnail-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {}
  [_ {:keys [helper info-text label required?]}]
  (if label [label.views/element {:content   label
                                  :helper    helper
                                  :info-text info-text
                                  :required? required?}]))

(defn- toggle-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:background-size (keyword)
  ;   :uri (string)(opt)}
  [thumbnail-id {:keys [background-size uri] :as thumbnail-props}]
  [:button.x-thumbnail--body (thumbnail.helpers/toggle-thumbnail-body-attributes thumbnail-id thumbnail-props)
                             [:div.x-thumbnail--icon  {:data-icon-family :material-icons-filled} :image]
                             [:div.x-thumbnail--image {:style {:background-image (css/url uri)
                                                               :background-size background-size}}]])

(defn- static-thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:background-size (keyword)
  ;   :uri (string)(opt)}
  [thumbnail-id {:keys [background-size uri] :as thumbnail-props}]
  [:div.x-thumbnail--body (thumbnail.helpers/static-thumbnail-body-attributes thumbnail-id thumbnail-props)
                          [:div.x-thumbnail--icon  {:data-icon-family :material-icons-filled} :image]
                          [:div.x-thumbnail--image {:style {:background-image (css/url uri)
                                                            :background-size background-size}}]])

(defn- thumbnail
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:on-click (metamorphic-event)(opt)}
  [thumbnail-id {:keys [on-click] :as thumbnail-props}]
  [:div.x-thumbnail (thumbnail.helpers/thumbnail-attributes thumbnail-id thumbnail-props)
                    [thumbnail-label thumbnail-id thumbnail-props]
                    (cond (some? on-click) [toggle-thumbnail thumbnail-id thumbnail-props]
                          (nil?  on-click) [static-thumbnail thumbnail-id thumbnail-props])])

(defn element
  ; @param (keyword)(opt) thumbnail-id
  ; @param (map) thumbnail-props
  ;  {:background-size (keyword)(opt)
  ;    :contain, :cover
  ;    Default: :contain
  ;   :border-radius (keyword)(opt)
  ;    :none, :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    Default: :none
  ;   :class (keyword or keywords in vector)(opt)
  ;   :disabled? (boolean)(opt)
  ;    Default: false
  ;   :height (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;    Default: :s
  ;   :helper (metamorphic-content)(opt)
  ;   :indent (map)(opt)
  ;    {:bottom (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :left (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :right (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;     :top (keyword)(opt)
  ;      :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;   :info-text (metamorphic-content)(opt)
  ;   :label (metamorphic-content)(opt)
  ;   :on-click (metamorphic-event)(opt)
  ;   :required? (boolean)(opt)
  ;    Default: false
  ;   :style (map)(opt)
  ;   :uri (string)(opt)
  ;   :width (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;    Default: :s}
  ;
  ; @usage
  ;  [thumbnail {...}]
  ;
  ; @usage
  ;  [thumbnail :my-thumbnail {...}]
  ([thumbnail-props]
   [element (random/generate-keyword) thumbnail-props])

  ([thumbnail-id thumbnail-props]
   (let [thumbnail-props (thumbnail.prototypes/thumbnail-props-prototype thumbnail-props)]
        [thumbnail thumbnail-id thumbnail-props])))
