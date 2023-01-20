
(ns elements.circle-diagram.views
    (:require [elements.circle-diagram.helpers    :as circle-diagram.helpers]
              [elements.circle-diagram.prototypes :as circle-diagram.prototypes]
              [elements.element.views             :as element.views]
              [random.api                         :as random]
              [svg.api                            :as svg]
              [vector.api                         :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- circle-diagram-section
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; @param (map) section-props
  [diagram-id diagram-props section-props]
  [:circle.e-circle-diagram--section (circle-diagram.helpers/section-attributes diagram-id diagram-props section-props)])

(defn- circle-diagram-sections
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:sections (maps in vector)}
  [diagram-id {:keys [sections] :as diagram-props}]
  (letfn [(f [sections section-props]
             (conj sections [circle-diagram-section diagram-id diagram-props section-props]))]
         (reduce f [:<>] sections)))

(defn circle-diagram-circle
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:diameter (px)}
  [diagram-id {:keys [diameter] :as diagram-props}]
  [:div.e-circle-diagram--body (circle-diagram.helpers/diagram-body-attributes diagram-id diagram-props)
                               [:svg (svg/wrapper-attributes  {:height diameter :width diameter})
                                     (circle-diagram-sections diagram-id diagram-props)]])

(defn circle-diagram
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  [diagram-id diagram-props]
  [:div.e-circle-diagram (circle-diagram.helpers/diagram-attributes diagram-id diagram-props)
                         [element.views/element-label               diagram-id diagram-props]
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
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :info-text (metamorphic-content)(opt)
  ;  :label (metamorphic-content)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
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
