
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.20
; Description:
; Version: v0.3.8
; Compatibility: x4.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.line-diagram
    (:require [mid-fruits.candy          :refer [param]]
              [mid-fruits.css            :as css]
              [mid-fruits.math           :as math]
              [mid-fruits.vector         :as vector]
              [x.app-components.api      :as components]
              [x.app-core.api            :as a]
              [x.app-elements.engine.api :as engine]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- diagram-props->total-value
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:sections (maps in vector)}
  ;
  ; @return (integer)
  [{:keys [sections]}]
  (reduce (fn [total-value {:keys [value]}]
              (+ total-value value))
          (param 0)
          (param sections)))

(defn- section-props->value-ratio
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:total-value (integer)}
  ; @param (map) section-props
  ;  {:value (integer)}
  ;
  ; @return (integer)
  [{:keys [total-value]} {:keys [value]}]
  (math/percent total-value value))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- section-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) section-props
  ;
  ; @return (map)
  ;  {:color (keyword)}
  [section-props]
  (merge {:color :primary}
         (param section-props)))

(defn- diagram-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) diagram-props
  ;  {:label (metamorphic-content)(opt)
  ;   :strength (integer)(opt)}
  ;
  ; @return (map)
  ;  {:color (keyword)
  ;   :font-size (keyword)
  ;   :label-position (keyword)
  ;   :layout (keyword)
  ;   :strength (px)
  ;   :total-value (integer)
  ;   :width (px)}
  [{:keys [label strength] :as diagram-props}]
  (merge {:layout   :row
          :strength 2
          :width    128}
         (if label {:color          :default
                    :font-size      :s
                    :label-position :left})
         {:total-value (diagram-props->total-value diagram-props)}
         (param diagram-props)
         (if strength {:strength (math/between! strength 1 6)})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- line-diagram-section
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (map) section-props
  ;  {:label (metamorphic-content)}
  ;
  ; @return (hiccup)
  [_ diagram-props {:keys [color label] :as section-props}]
  (let [value-ratio (section-props->value-ratio diagram-props section-props)]
       [:div.x-line-diagram--section {:data-color    (param color)
                                      :style {:width (css/percent value-ratio)}}]))

(defn- line-diagram-sections
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:sections (maps in vector)
  ;   :strength (px)}
  ;
  ; @return (hiccup)
  [diagram-id {:keys [sections strength] :as diagram-props}]
  (reduce (fn [line-diagram-sections section-props]
              (let [section-props (section-props-prototype section-props)]
                   (vector/conj-item line-diagram-sections
                                     [line-diagram-section diagram-id diagram-props section-props])))
          [:div.x-line-diagram--sections {:style {:height (css/px strength)}}]
          (param sections)))

(defn- line-diagram-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;  {:label (metamorphic-content)(opt)
  ;   :label-position (keyword)}
  ;
  ; @return (hiccup)
  [_ {:keys [label label-position]}]
  (if label [:div.x-line-diagram--label {:data-position label-position}
                                        [components/content {:content label}]]))

(defn line-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ;
  ; @return (hiccup)
  [diagram-id diagram-props]
  [:div.x-line-diagram (engine/element-attributes diagram-id diagram-props)
                       [line-diagram-label        diagram-id diagram-props]
                       [line-diagram-sections     diagram-id diagram-props]])

(defn element
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ;  {:color (keyword)(opt)
  ;    :default, :muted, :primary, :secondary
  ;    Default: :default
  ;    Only w/ {:label ...}
  ;   :font-size (keyword)(opt)
  ;    :xxs, :xs, :s, :m
  ;    Default: :s
  ;    Only w/ {:label ...}
  ;   :indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :label (metamorphic-content)(opt)
  ;   :label-position (keyword)(opt)
  ;    :left, :center :right
  ;    Default: :left
  ;    Only w/ {:label ...}
  ;   :layout (keyword)(opt)
  ;    :fit, :row
  ;    Default: :row
  ;   :sections (maps in vector)}
  ;    [{:color (keyword)(opt)
  ;       :default, :highlight, :muted, :primary, :secondary, :success, :warning
  ;       Default: primary
  ;      :label (metamorphic-content)(opt)
  ;       TODO ...
  ;      :value (integer)}]
  ;   :strength (px)(opt)
  ;     Default: 2
  ;     Min: 1
  ;     Max: 6
  ;   :total-value (integer)(opt)
  ;    Default: A szakaszok aktuális értékének összege
  ;   :width (px)(opt)
  ;    Default: 128}
  ;
  ; @usage
  ;  [elements/line-diagram {...}]
  ;
  ; @usage
  ;  [elements/line-diagram :my-line-diagram {...}]
  ;
  ; @return (component)
  ([diagram-props]
   [element (a/id) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (diagram-props-prototype diagram-props)]
        [line-diagram diagram-id diagram-props])))
