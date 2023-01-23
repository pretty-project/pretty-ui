
(ns elements.horizontal-polarity.views
    (:require [elements.horizontal-polarity.attributes :as horizontal-polarity.attributes]
              [elements.horizontal-polarity.prototypes :as horizontal-polarity.prototypes]
              [random.api                              :as random]
              [x.components.api                        :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- horizontal-polarity
  ; @ignore
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ; {:end-content (metamorphic-content)(opt)
  ;  :middle-content (metamorphic-content)(opt)
  ;  :start-content (metamorphic-content)(opt)}
  [polarity-id {:keys [end-content middle-content start-content] :as polarity-props}]
  [:div (horizontal-polarity.attributes/polarity-attributes polarity-id polarity-props)
        [:div (horizontal-polarity.attributes/polarity-body-attributes polarity-id polarity-props)
              (if start-content  [:div {:class :e-horizontal-polarity--start-content}
                                       [x.components/content start-content]]
                                 [:div {:class :e-horizontal-polarity--placeholder}])
              (if middle-content [:div {:class :e-horizontal-polarity--middle-content}
                                       [x.components/content middle-content]]
                                 [:div {:class :e-horizontal-polarity--placeholder}])
              (if end-content    [:div {:class :e-horizontal-polarity--end-content}
                                       [x.components/content end-content]]
                                 [:div {:class :e-horizontal-polarity--placeholder}])]])

(defn element
  ; @param (keyword)(opt) polarity-id
  ; @param (map) polarity-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :end-content (metamorphic-content)
  ;  :indent (map)(opt)
  ;   {:bottom (keyword)(opt)
  ;    :left (keyword)(opt)
  ;    :right (keyword)(opt)
  ;    :top (keyword)(opt)
  ;    :horizontal (keyword)(opt)
  ;    :vertical (keyword)(opt)
  ;     :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl}
  ;  :middle-content (metamorphic-content)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :style (map)(opt)
  ;  :start-content (metamorphic-content)(opt)
  ;  :vertical-align (keyword)(opt)
  ;   :bottom, :center, :top
  ;   Default: :center}
  ;
  ; @usage
  ; [horizontal-polarity {...}]
  ;
  ; @usage
  ; [horizontal-polarity :my-horizontal-polarity {...}]
  ;
  ; @usage
  ; [horizontal-polarity {:start-content [:<> [label {:content "My label"}]
  ;                                           [label {:content "My label"}]]}]
  ([polarity-props]
   [element (random/generate-keyword) polarity-props])

  ([polarity-id polarity-props]
   (let [polarity-props (horizontal-polarity.prototypes/polarity-props-prototype polarity-props)]
        [horizontal-polarity polarity-id polarity-props])))
