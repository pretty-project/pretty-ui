
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.element-components.line-diagram
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
  ;  {:strength (integer)(opt)}
  ;
  ; @return (map)
  ;  {:strength (px)
  ;   :total-value (integer)}
  [{:keys [strength] :as diagram-props}]
  (merge {:strength 2
          :total-value (diagram-props->total-value diagram-props)}
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
  [diagram-id {:keys [sections strength] :as diagram-props}]
  (reduce (fn [line-diagram-sections section-props]
              (let [section-props (section-props-prototype section-props)]
                   (vector/conj-item line-diagram-sections
                                     [line-diagram-section diagram-id diagram-props section-props])))
          [:div.x-line-diagram--sections {:style {:height (css/px strength)}}]
          (param sections)))

(defn line-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div.x-line-diagram (engine/element-attributes diagram-id diagram-props)
                       [line-diagram-sections     diagram-id diagram-props]])

(defn element
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ;  {:indent (keyword)(opt)
  ;    :left, :right, :both, :none
  ;    Default: :none
  ;   :min-height (keyword)(opt)
  ;    :xxs, :xs, :s, :m, :l, :xl, :xxl
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
  ;    Default: A szakaszok aktuális értékének összege}
  ;
  ; @usage
  ;  [elements/line-diagram {...}]
  ;
  ; @usage
  ;  [elements/line-diagram :my-line-diagram {...}]
  ([diagram-props]
   [element (a/id) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (diagram-props-prototype diagram-props)]
        [line-diagram diagram-id diagram-props])))