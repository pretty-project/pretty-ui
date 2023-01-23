
(ns elements.line-diagram.views
    (:require [elements.line-diagram.attributes :as line-diagram.attributes]
              [elements.line-diagram.prototypes :as line-diagram.prototypes]
              [hiccup.api                       :as hiccup]
              [random.api                       :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn line-diagram
  ; @ignore
  ;
  ; @param (keyword) diagram-id
  ; @param (map) diagram-props
  ; {:sections (maps in vector)}
  [diagram-id {:keys [sections] :as diagram-props}]
  [:div (line-diagram.attributes/diagram-attributes diagram-id diagram-props)
        (letfn [(f [section-props] (let [section-props (line-diagram.prototypes/section-props-prototype section-props)]
                                        [:div (line-diagram.attributes/diagram-section-attributes diagram-id diagram-props section-props)]))]
               [:div (line-diagram.attributes/diagram-sections-attributes diagram-id diagram-props)
                     (hiccup/put-with [:<>] sections f)])])

(defn element
  ; @param (keyword)(opt) diagram-id
  ; @param (map) diagram-props
  ; {:indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :sections (maps in vector)}
  ;   [{:color (keyword or string)(opt)
  ;      :default, :highlight, :invert, :muted, :primary, :secondary, :success, :warning
  ;      Default: primary
  ;     :label (metamorphic-content)(opt)
  ;      TODO
  ;     :value (integer)}]
  ;  :strength (px)(opt)
  ;    Default: 2
  ;    Min: 1
  ;    Max: 6
  ;  :total-value (integer)(opt)
  ;   Default: A szakaszok aktuális értékének összege}
  ;
  ; @usage
  ; [line-diagram {...}]
  ;
  ; @usage
  ; [line-diagram :my-line-diagram {...}]
  ([diagram-props]
   [element (random/generate-keyword) diagram-props])

  ([diagram-id diagram-props]
   (let [diagram-props (line-diagram.prototypes/diagram-props-prototype diagram-props)]
        [line-diagram diagram-id diagram-props])))
