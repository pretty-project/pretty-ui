
(ns elements.circle-diagram.views
    (:require [elements.circle-diagram.helpers    :as circle-diagram.helpers]
              [elements.circle-diagram.prototypes :as circle-diagram.prototypes]
              [elements.label.views               :as label.views]
              [random.api                         :as random]
              [svg.api                            :as svg]
              [vector.api                         :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- circle-diagram-section
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (map) section-props
  [diagram-id diagram-props section-props]
  [:circle.e-circle-diagram--section (circle-diagram.helpers/section-attributes diagram-id diagram-props section-props)])

(defn- circle-diagram-sections
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:sections (maps in vector)}
  [diagram-id {:keys [sections] :as diagram-props}]
  (letfn [(f [sections section-props]
             (conj sections [circle-diagram-section diagram-id diagram-props section-props]))]
         (reduce f [:<>] sections)))

(defn circle-diagram-circle
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:diameter (px)}
  [diagram-id {:keys [diameter] :as diagram-props}]
  [:div.e-circle-diagram--body (circle-diagram.helpers/diagram-body-attributes diagram-id diagram-props)
                               [:svg (svg/wrapper-attributes  {:height diameter :width diameter})
                                     (circle-diagram-sections diagram-id diagram-props)]])

(defn- circle-diagram-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:helper (metamorphic-content)(opt)
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)}
  [_ {:keys [helper info-text label]}]
  (if label [label.views/element {:content     label
                                  :helper      helper
                                  :info-text   info-text
                                  :line-height :block}]))

(defn circle-diagram
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div.e-circle-diagram (circle-diagram.helpers/diagram-attributes diagram-id diagram-props)
                         [circle-diagram-label                      diagram-id diagram-props]
                         [circle-diagram-circle                     diagram-id diagram-props]])

(defn element
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :diameter (px)(opt)
  ;   Default: 48
  ;  :helper (metamorphic-content)(opt)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :left (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :right (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl
  ;    :top (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl}
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
  ;  :sections (maps in vector)}
  ;   [{:color (keyword or string)
  ;      :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;     :label (metamorphic-content)(opt)
  ;      TODO
  ;     :value (integer)}]
  ;  :strength (px)(opt)
  ;    Default: 2
  ;    Min: 1
  ;    Max: 6
  ;  :style (map)(opt)}
  ;
  ; @usage
  ; [circle-diagram {...}]
  ;
  ; @usage
  ; [circle-diagram :my-circle-diagram {...}]
  ([diagram-props]
   [element (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (circle-diagram.prototypes/diagram-props-prototype diagram-props)]
        [circle-diagram diagram-id diagram-props])))
