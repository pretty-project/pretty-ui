
(ns pretty-elements.horizontal-polarity.views
    (:require [fruits.random.api                              :as random]
              [metamorphic-content.api                        :as metamorphic-content]
              [pretty-elements.horizontal-polarity.attributes :as horizontal-polarity.attributes]
              [pretty-elements.horizontal-polarity.prototypes :as horizontal-polarity.prototypes]
              [pretty-presets.api                             :as pretty-presets]))

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
              (if start-content  [:div {:class :pe-horizontal-polarity--start-content}
                                       [metamorphic-content/compose start-content]]
                                 [:div {:class :pe-horizontal-polarity--placeholder}])
              (if middle-content [:div {:class :pe-horizontal-polarity--middle-content}
                                       [metamorphic-content/compose middle-content]]
                                 [:div {:class :pe-horizontal-polarity--placeholder}])
              (if end-content    [:div {:class :pe-horizontal-polarity--end-content}
                                       [metamorphic-content/compose end-content]]
                                 [:div {:class :pe-horizontal-polarity--placeholder}])]])

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
  ;   Same as the :indent property.
  ;  :preset (keyword)(opt)
  ;  :style (map)(opt)
  ;  :start-content (metamorphic-content)(opt)
  ;  :vertical-align (keyword)(opt)
  ;   :bottom, :center, :top
  ;   Default: :center
  ;  :width (keyword)(opt)
  ;   :auto, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :auto}
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
   (fn [_ polarity-props] ; XXX#0106 (README.md#parametering)
       (let [polarity-props (pretty-presets/apply-preset                             polarity-props)
             polarity-props (horizontal-polarity.prototypes/polarity-props-prototype polarity-props)]
            [horizontal-polarity polarity-id polarity-props]))))
