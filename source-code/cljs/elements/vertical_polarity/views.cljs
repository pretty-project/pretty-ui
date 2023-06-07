
(ns elements.vertical-polarity.views
    (:require [elements.vertical-polarity.attributes :as vertical-polarity.attributes]
              [elements.vertical-polarity.prototypes :as vertical-polarity.prototypes]
              [metamorphic-content.api               :as metamorphic-content]
              [random.api                            :as random]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- vertical-polarity
  ; @ignore
  ;
  ; @param (keyword) polarity-id
  ; @param (map) polarity-props
  ; {:end-content (metamorphic-content)
  ;  :middle-content (metamorphic-content)
  ;  :start-content (metamorphic-content)}
  [polarity-id {:keys [end-content middle-content start-content] :as polarity-props}]
  [:div (vertical-polarity.attributes/polarity-attributes polarity-id polarity-props)
        [:div (vertical-polarity.attributes/polarity-body-attributes polarity-id polarity-props)
              (if start-content  [:div {:class :e-vertical-polarity--start-content}
                                       [metamorphic-content/compose start-content]]
                                 [:div {:class :e-vertical-polarity--placeholder}])
              (if middle-content [:div {:class :e-vertical-polarity--middle-content}
                                       [metamorphic-content/compose middle-content]]
                                 [:div {:class :e-vertical-polarity--placeholder}])
              (if end-content    [:div {:class :e-vertical-polarity--end-content}
                                       [metamorphic-content/compose end-content]]
                                 [:div {:class :e-vertical-polarity--placeholder}])]])

(defn element
  ; @param (keyword)(opt) polarity-id
  ; @param (map) polarity-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :end-content (metamorphic-content)
  ;  :height (keyword)(opt)
  ;   :auto, :content, :parent, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :parent
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :center
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
  ;  :style (map)(opt)
  ;  :start-content (metamorphic-content)(opt)
  ;
  ; @usage
  ; [vertical-polarity {...}]
  ;
  ; @usage
  ; [vertical-polarity :my-vertical-polarity {...}]
  ;
  ; @usage
  ; [vertical-polarity {:start-content [:<> [label {:content "My label"}]
  ;                                         [label {:content "My label"}]]}]
  ([polarity-props]
   [element (random/generate-keyword) polarity-props])

  ([polarity-id polarity-props]
   (let [polarity-props (vertical-polarity.prototypes/polarity-props-prototype polarity-props)]
        [vertical-polarity polarity-id polarity-props])))
