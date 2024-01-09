
(ns pretty-build-kit.attributes
    (:require [fruits.map.api :as map]
              [fruits.vector.api :as vector]
              [fruits.mixed.api :as mixed]
              [fruits.hiccup.api :as hiccup]
              [pretty-build-kit.utils :as utils]
              [pretty-build-kit.side-effects :as side-effects]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn border-radius-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:border-radius (map)(opt)
  ;   {:tl (keyword, px or string)(opt)
  ;    :tr (keyword, px or string)(opt)
  ;    :br (keyword, px or string)(opt)
  ;    :bl (keyword, px or string)(opt)
  ;    :all (keyword, px or string)(opt)}}
  ;
  ; @usage
  ; (border-radius-attributes {...} {:border-radius {:tr :xxl :tl :xs}})
  ; =>
  ; {:data-border-radius-tr :xxl
  ;  :data-border-radius-tl :xs
  ;  ...}
  ;
  ; @return (map)
  ; {:data-border-radius-all (keyword)
  ;  :data-border-radius-bl (keyword)
  ;  :data-border-radius-br (keyword)
  ;  :data-border-radius-tl (keyword)
  ;  :data-border-radius-tr (keyword)
  ;  :style (map)
  ;   {"--border-radius-all" (string)
  ;    "--border-radius-bl" (string)
  ;    "--border-radius-br" (string)
  ;    "--border-radius-tl" (string)
  ;    "--border-radius-tr" (string)}}
  [element-attributes {:keys [border-radius]}]
  (letfn [(f0 [result k v]
              (let [css-var-name        (keyword (str      "border-radius-" (name k)))
                    data-attribute-name (keyword (str "data-border-radius-" (name k)))]
                   (utils/apply-property-value result css-var-name data-attribute-name v "px")))]
         (merge element-attributes (if (map?            border-radius)
                                       (reduce-kv f0 {} border-radius)))))

(defn border-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:border-color (keyword or string)(opt)
  ;  :border-radius (map)(opt)
  ;   {:tl (keyword, px or string)(opt)
  ;    :tr (keyword, px or string)(opt)
  ;    :br (keyword, px or string)(opt)
  ;    :bl (keyword, px or string)(opt)
  ;    :all (keyword, px or string)(opt)}}
  ;  :border-position (keyword)(opt)
  ;  :border-width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (border-attributes {...} {:border-color :default :border-width :s})
  ; =>
  ; {:data-border-color :default
  ;  :data-border-width :s
  ;  ...}
  ;
  ; @return (map)
  ; {:data-border-color (keyword)
  ;  :data-border-position (keyword)
  ;  :data-border-width (keyword)
  ;  :style (map)
  ;   {"--border-color" (string)
  ;    "--border-width" (string)}}
  [element-attributes {:keys [border-color border-position border-width] :as element-props}]
  (-> (map/merge-some element-attributes {:data-border-position border-position})
      (border-radius-attributes element-props)
      (utils/apply-property-value :border-color :data-border-color border-color)
      (utils/apply-property-value :border-width :data-border-width border-width "px")))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn block-max-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (block-max-size-attributes {...} {:max-height :xl, :max-width :xl})
  ; =>
  ; {:data-max-block-height :xl
  ;  :data-max-block-width  :xl
  ;  ...}
  ;
  ; @return (map)
  ; {:data-block-max-height (keyword)
  ;  :data-block-max-width (keyword)
  ;  :style (map)
  ;   {"--block-max-height" (string)
  ;    "--block-max-width" (string)}}
  [element-attributes {:keys [max-height max-width]}]
  (-> element-attributes (utils/apply-property-value :block-max-height :data-block-max-height max-height)
                         (utils/apply-property-value :block-max-width  :data-block-max-width  max-width)))

(defn block-min-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (block-min-size-attributes {...} {:min-height :xl, :min-width :xl})
  ; =>
  ; {:data-block-min-height :xl
  ;  :data-block-min-width  :xl
  ;  ...}
  ;
  ; @return (map)
  ; {:data-block-min-height (keyword)
  ;  :data-block-min-width (keyword)
  ;  :style (map)
  ;   {"--block-min-height" (string)
  ;    "--block-min-width" (string)}}
  [element-attributes {:keys [min-height min-width]}]
  (-> element-attributes (utils/apply-property-value :block-min-height :data-block-min-height min-height)
                         (utils/apply-property-value :block-min-width  :data-block-min-width  min-width)))

(defn block-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:height (keyword, px or string)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (block-size-attributes {...} {:height :xl, :width :xl})
  ; =>
  ; {:data-block-height :xl
  ;  :data-block-width  :xl
  ;  ...}
  ;
  ; @return (map)
  ; {:data-block-height (keyword)
  ;  :data-block-width (keyword)
  ;  :style (map)
  ;   {"--block-height" (string)
  ;    "--block-width" (string)}}
  [element-attributes {:keys [height width]}]
  (-> element-attributes (utils/apply-property-value :block-height :data-block-height height)
                         (utils/apply-property-value :block-width  :data-block-width  width)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn content-max-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (content-max-size-attributes {...} {:max-height :xl, :max-width :xl})
  ; =>
  ; {:data-content-max-height :xl
  ;  :data-content-max-width  :xl
  ;  ...}
  ;
  ; @return (map)
  ; {:data-content-max-height (keyword)
  ;  :data-content-max-width (keyword)
  ;  :style (map)
  ;   {"--content-max-height" (string)
  ;    "--content-max-width" (string)}}
  [element-attributes {:keys [max-height max-width]}]
  (-> element-attributes (utils/apply-property-value :content-max-height :data-content-max-height max-height)
                         (utils/apply-property-value :content-max-width  :data-content-max-width  max-width)))

(defn content-min-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (content-min-size-attributes {...} {:min-height :xl, :min-width :xl})
  ; =>
  ; {:data-content-min-height :xl
  ;  :data-content-min-width  :xl
  ;  ...}
  ;
  ; @return (map)
  ; {:data-content-min-height (keyword)
  ;  :data-content-min-width (keyword)
  ;  :style (map)
  ;   {"--content-min-height" (string)
  ;    "--content-min-width" (string)}}
  [element-attributes {:keys [min-height min-width]}]
  (-> element-attributes (utils/apply-property-value :content-min-height :data-content-min-height min-height)
                         (utils/apply-property-value :content-min-width  :data-content-min-width  min-width)))

(defn content-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:height (keyword, px or string)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (content-size-attributes {...} {:height :xl, :width :xl})
  ; =>
  ; {:data-content-height :xl
  ;  :data-content-width  :xl
  ;  ...}
  ;
  ; @return (map)
  ; {:data-content-height (keyword)
  ;  :data-content-width (keyword)
  ;  :style (map)
  ;   {"--content-height" (string)
  ;    "--content-width" (string)}}
  [element-attributes {:keys [height width]}]
  (-> element-attributes (utils/apply-property-value :content-height :data-content-height height)
                         (utils/apply-property-value :content-width  :data-content-width  width)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-max-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:max-height (keyword, px or string)(opt)
  ;  :max-width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (element-max-size-attributes {...} {:max-height :xl, :max-width :xl})
  ; =>
  ; {:data-element-max-height :xl
  ;  :data-element-max-width  :xl
  ;  ...}
  ;
  ; @return (map)
  ; {:data-element-max-height (keyword)
  ;  :data-element-max-width (keyword)
  ;  :style (map)
  ;   {"--element-max-height" (string)
  ;    "--element-max-width" (string)}}
  [element-attributes {:keys [max-height max-width]}]
  (-> element-attributes (utils/apply-property-value :element-max-height :data-element-max-height max-height)
                         (utils/apply-property-value :element-max-width  :data-element-max-width  max-width)))

(defn element-min-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:min-height (keyword, px or string)(opt)
  ;  :min-width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (element-min-size-attributes {...} {:min-height :xl, :min-width :xl})
  ; =>
  ; {:data-element-min-height :xl
  ;  :data-element-min-width  :xl
  ;  ...}
  ;
  ; @return (map)
  ; {:data-element-min-height (keyword)
  ;  :data-element-min-width (keyword)
  ;  :style (map)
  ;   {"--element-min-height" (string)
  ;    "--element-min-width" (string)}}
  [element-attributes {:keys [min-height min-width]}]
  (-> element-attributes (utils/apply-property-value :element-min-height :data-element-min-height min-height)
                         (utils/apply-property-value :element-min-width  :data-element-min-width  min-width)))

(defn element-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:height (keyword, px or string)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (element-size-attributes {...} {:height :xl, :width :xl})
  ; =>
  ; {:data-element-height :xl
  ;  :data-element-width  :xl
  ;  ...}
  ;
  ; @return (map)
  ; {:data-element-height (keyword)
  ;  :data-content-width (keyword)
  ;  :style (map)
  ;   {"--element-height" (string)
  ;    "--element-width" (string)}}
  [element-attributes {:keys [height width]}]
  (-> element-attributes (utils/apply-property-value :element-height :data-element-height height)
                         (utils/apply-property-value :element-width  :data-element-width  width)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn wrapper-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:height (keyword, px or string)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (wrapper-size-attributes {...} {:height :auto, :width :parent})
  ; =>
  ; {:data-wrapper-height :auto
  ;  :data-wrapper-width  :parent
  ;  ...}
  ;
  ; @return (map)
  ; {:data-wrapper-height (keyword)
  ;  :data-wrapper-width (keyword)}
  [element-attributes {:keys [height width]}]
  ; In case the element height / element width is ':auto' or ':parent', the wrapper element's
  ; height and width must be set as well. Otherwise, the wrapper would prevent the element expanding.
  (merge element-attributes (case height :auto   {:data-wrapper-height :auto}
                                         :parent {:data-wrapper-height :parent}
                                                 {:data-wrapper-height :content})
                            (case width  :auto   {:data-wrapper-width  :auto}
                                         :parent {:data-wrapper-width  :parent}
                                                 {:data-wrapper-width  :content})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn thumbnail-size-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:height (keyword, px or string)(opt)
  ;  :width (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (thumbnail-size-attributes {...} {:height :xl, :width :xl})
  ; =>
  ; {:data-thumbnail-height :xl
  ;  :data-thumbnail-width  :xl
  ;  ...}
  ;
  ; @return (map)
  ; {:data-thumbnail-height (keyword)
  ;  :data-thumbnail-width (keyword)
  ;  :style (map)
  ;   {"--thumbnail-height" (string)
  ;    "--thumbnail-width" (string)}}
  [element-attributes {:keys [height width]}]
  (-> element-attributes (utils/apply-property-value :thumbnail-height :data-thumbnail-height height)
                         (utils/apply-property-value :thumbnail-width  :data-thumbnail-width  width)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn indent-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:indent (map)(opt)
  ;   {:bottom (keyword, px or string)(opt)
  ;    :left (keyword, px or string)(opt)
  ;    :right (keyword, px or string)(opt)
  ;    :top (keyword, px or string)(opt)
  ;    :horizontal (keyword, px or string)(opt)
  ;    :vertical (keyword, px or string)(opt)
  ;    :all (keyword, px or string)(opt)}}
  ;
  ; @usage
  ; (indent-attributes {...} {:indent {:horizontal :xxl :left :xs}})
  ; =>
  ; {:data-indent-horizontal :xxl
  ;  :data-indent-left       :xs
  ;  ...}
  ;
  ; @return (map)
  ; {:data-indent-bottom (keyword)
  ;  :data-indent-horizontal (keyword)
  ;  :data-indent-left (keyword)
  ;  :data-indent-right (keyword)
  ;  :data-indent-top (keyword)
  ;  :data-indent-vertical (keyword)
  ;  :style (map)
  ;   {"--indent-bottom" (string)
  ;    "--indent-horizontal" (string)
  ;    "--indent-left" (string)
  ;    "--indent-right" (string)
  ;    "--indent-top" (string)
  ;    "--indent-vertical" (string)}}
  [element-attributes {:keys [indent]}]
  (letfn [(f0 [result k v]
              (let [css-var-name        (keyword (str      "indent-" (name k)))
                    data-attribute-name (keyword (str "data-indent-" (name k)))]
                   (utils/apply-property-value result css-var-name data-attribute-name v "px")))]
         (merge element-attributes (if (map?            indent)
                                       (reduce-kv f0 {} indent)))))

(defn outdent-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:outdent (map)(opt)
  ;   {:bottom (keyword, px or string)(opt)
  ;    :left (keyword, px or string)(opt)
  ;    :right (keyword, px or string)(opt)
  ;    :top (keyword, px or string)(opt)
  ;    :horizontal (keyword, px or string)(opt)
  ;    :vertical (keyword, px or string)(opt)
  ;    :all (keyword, px or string)(opt)}}
  ;
  ; @usage
  ; (outdent-attributes {...} {:outdent {:horizontal :xxl :left :xs}})
  ; =>
  ; {:data-outdent-horizontal :xxl
  ;  :data-outdent-left       :xs
  ;  ...}
  ;
  ; @return (map)
  ; {:data-outdent-bottom (keyword)
  ;  :data-outdent-horizontal (keyword)
  ;  :data-outdent-left (keyword)
  ;  :data-outdent-right (keyword)
  ;  :data-outdent-top (keyword)
  ;  :data-outdent-vertical (keyword)
  ;  :style (map)
  ;   {"--outdent-bottom" (string)
  ;    "--outdent-horizontal" (string)
  ;    "--outdent-left" (string)
  ;    "--outdent-right" (string)
  ;    "--outdent-top" (string)
  ;    "--outdent-vertical" (string)}}
  [element-attributes {:keys [outdent]}]
  (letfn [(f0 [result k v]
              (let [css-var-name        (keyword (str      "outdent-" (name k)))
                    data-attribute-name (keyword (str "data-outdent-" (name k)))]
                   (utils/apply-property-value result css-var-name data-attribute-name v "px")))]
         (merge element-attributes (if (map?            outdent)
                                       (reduce-kv f0 {} outdent)))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn column-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:horizontal-align (keyword)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :wrap-items? (boolean)(opt)}
  ;
  ; @usage
  ; (column-attributes {...} {:horizontal-align :left :vertical-align :space-between})
  ; =>
  ; {:data-column-horizontal-align :left
  ;  :data-column-vertical-align   :space-between
  ;  ...}
  ;
  ; @return (map)
  ; {:data-column-horizontal-align (keyword)
  ;  :data-column-vertical-align (keyword)
  ;  :data-row-gap (keyword)
  ;  :data-wrap-items (boolean)
  ;  :style (map)
  ;   {"--row-gap" (string}
  [element-attributes {:keys [horizontal-align gap vertical-align wrap-items?]}]
  ; The 'row-gap' property between column attributes is not by mistake!
  ; Flex columns contain rows in a column.
  (-> element-attributes (map/merge-some {:data-column-horizontal-align horizontal-align
                                          :data-column-vertical-align   vertical-align
                                          :data-wrap-items              wrap-items?})
                         (utils/apply-property-value :row-gap :data-row-gap gap "px")))

(defn row-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:horizontal-align (keyword)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :vertical-align (keyword)(opt)
  ;  :wrap-items? (boolean)(opt)}
  ;
  ; @usage
  ; (row-attributes {...} {:horizontal-align :left :vertical-align :space-between})
  ; =>
  ; {:data-row-horizontal-align :left
  ;  :data-row-vertical-align   :space-between
  ;  ...}
  ;
  ; @return (map)
  ; {:data-column-gap (keyword)
  ;  :data-row-horizontal-align (keyword)
  ;  :data-row-vertical-align (keyword)
  ;  :data-wrap-items (boolean)
  ;  :style (map)
  ;   {"--column-gap" (string}
  [element-attributes {:keys [horizontal-align gap vertical-align wrap-items?]}]
  ; The 'column-gap' property between row attributes is not by mistake!
  ; Flex rows contain columns in a row.
  (-> element-attributes (map/merge-some {:data-row-horizontal-align horizontal-align
                                          :data-row-vertical-align   vertical-align
                                          :data-wrap-items           wrap-items?})
                         (utils/apply-property-value :column-gap :data-column-gap gap "px")))

(defn grid-attributes
  ; @important
  ; This function is incomplete and may not behave as expected.
  ;
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:column-gap (keyword, px or string)(opt)
  ;  :gap (keyword, px or string)(opt)
  ;  :row-gap (keyword, px or string)(opt)}
  ;
  ; @usage
  ; ...
  ;
  ; @return (map)
  ; {}
  [element-attributes {:keys [column-gap gap row-gap]}]
  ; TODO
  (-> element-attributes (map/merge-some {})
                         (utils/apply-property-value :gap        :data-gap        gap        "px")
                         (utils/apply-property-value :column-gap :data-column-gap column-gap "px")
                         (utils/apply-property-value :row-gap    :data-row-gap    row-gap    "px")))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn color-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:disabled? (boolean)(opt)
  ;  :fill-color (keyword or string)(opt)
  ;  :fill-pattern (keyword)(opt)
  ;  :hover-color (keyword or string)(opt)
  ;  :text-color (keyword or string)(opt)}
  ;
  ; @usage
  ; (color-attributes {...} {:fill-color :highlight :hover-color :highlight :text-color :default})
  ; =>
  ; {:data-fill-color  :highlight
  ;  :data-hover-color :highlight
  ;  :data-text-color  :default
  ;  ...}
  ;
  ; @return (map)
  ; {:data-fill-color (keyword)
  ;  :data-fill-pattern (keyword)
  ;  :data-hover-color (keyword)
  ;  :data-text-color (keyword)
  ;  :style (map)
  ;   {"--fill-color" (string)
  ;    "--hover-color" (string)
  ;    "--text-color" (string)}}
  [element-attributes {:keys [disabled? fill-color fill-pattern hover-color text-color]}]
  ; Automatically turns off the hover color on disabled elements.
  (if disabled? (-> element-attributes (map/merge-some {:data-fill-pattern fill-pattern})
                                       (utils/apply-property-value :fill-color  :data-fill-color  fill-color)
                                       (utils/apply-property-value :text-color  :data-text-color  text-color))
                (-> element-attributes (map/merge-some {:data-fill-pattern fill-pattern})
                                       (utils/apply-property-value :fill-color  :data-fill-color  fill-color)
                                       (utils/apply-property-value :hover-color :data-hover-color hover-color)
                                       (utils/apply-property-value :text-color  :data-text-color  text-color))))

(defn cursor-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:cursor (keyword or string)(opt)
  ;  :disabled? (boolean)(opt)}
  ;
  ; @usage
  ; (cursor-attributes {...} {:cursor :pointer})
  ; =>
  ; {:data-cursor :pointer
  ;  ...}
  ;
  ; @return (map)
  ; {:data-cursor (keyword)
  ;  :style (map)
  ;   "--cursor" (string)}
  [element-attributes {:keys [cursor disabled?]}]
  ; Automatically applies the default cursor on disabled elements in case of no specific cursor value is provided.
  (if disabled? (utils/apply-property-value element-attributes :cursor :data-cursor (or cursor :default))
                (utils/apply-property-value element-attributes :cursor :data-cursor (-> cursor))))

(defn font-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:font-size (keyword, px or string)(opt)
  ;  :font-weight (keyword, px or string)(opt)
  ;  :letter-spacing (keyword, px or string)(opt)
  ;  :line-height (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (font-attributes {...} {:font-size :s :font-weight :bold :letter-spacing :s :line-height :text-block})
  ; =>
  ; {:data-font-size      :s
  ;  :data-font-weight    :bold
  ;  :data-letter-spacing :s
  ;  :data-line-height    :text-block
  ;  ...}
  ;
  ; @return (map)
  ; {:data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-letter-spacing (keyword)
  ;  :data-line-height (keyword)
  ;  :style (map)
  ;   {"--font-size" (string)
  ;    "--font-weight" (string)
  ;    "--letter-spacing" (string)
  ;    "--line-height" (string)}}
  [element-attributes {:keys [font-size font-weight letter-spacing line-height]}]
  (-> element-attributes (utils/apply-property-value :font-size      :data-font-size      font-size      "px")
                         (utils/apply-property-value :letter-spacing :data-letter-spacing letter-spacing "px")
                         (utils/apply-property-value :line-height    :data-line-height    line-height    "px")
                         (utils/apply-property-value :font-weight    :data-font-weight    font-weight)))

(defn icon-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:icon-color (keyword or string)(opt)
  ;  :icon-family (keyword)(opt)
  ;  :icon-size (keyword, px or string)(opt)}
  ;
  ; @usage
  ; (icon-attributes {...} {:icon-color :default :icon-family :my-icon-family :icon-size :s})
  ; =>
  ; {:data-icon-color  :default
  ;  :data-icon-family :my-icon-family
  ;  :data-icon-size   :s
  ;  ...}
  ;
  ; @return (map)
  ; {:data-icon-color (keyword)
  ;  :data-icon-family (keyword)
  ;  :data-icon-size (keyword)
  ;  :style (map)
  ;   {"--icon-color" (string)
  ;    "--icon-size" (string)}}
  [element-attributes {:keys [icon-color icon-family icon-size]}]
  (-> (map/merge-some element-attributes {:data-icon-family icon-family})
      (utils/apply-property-value :icon-color :data-icon-color icon-color)
      (utils/apply-property-value :icon-size  :data-icon-size  icon-size "px")))

(defn text-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:selectable? (boolean)(opt)
  ;  :text-align (keyword)(opt)
  ;  :text-direction (keyword)(opt)
  ;  :text-overflow (keyword)(opt)
  ;  :text-transform (keyword)(opt)}
  ;
  ; @usage
  ; (text-attributes {...} {:text-direction :reversed :text-overflow :ellipsis})
  ; =>
  ; {:data-text-direction :default
  ;  :data-text-overflow  :ellipsis
  ;  ...}
  ;
  ; @return (map)
  ; {:data-selectable (boolean)
  ;  :data-text-horizontal-align (keyword)
  ;  :data-text-direction (keyword)
  ;  :data-text-overflow (keyword)}
  ;  :data-text-transform (keyword)}
  [element-attributes {:keys [selectable? text-align text-direction text-overflow text-transform]}]
  (map/merge-some element-attributes {:data-selectable            selectable?
                                      :data-text-horizontal-align text-align
                                      :data-text-direction        text-direction
                                      :data-text-overflow         text-overflow
                                      :data-text-transform        text-transform}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn class-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:class (keyword or keywords in vector)(opt)}
  ;
  ; @usage
  ; (class-attributes {...} {:class :my-class})
  ; =>
  ; {:class :my-class
  ;  ...}
  ;
  ; @usage
  ; (class-attributes {...} {:class [:my-class]})
  ; =>
  ; {:class [:my-class]
  ;  ...}
  ;
  ; @return (map)
  ; {:class (keywords in vector)}
  [element-attributes {:keys [class] :as element-props}]
  (assoc element-attributes :class (vector/concat-items (-> element-attributes :class mixed/to-vector)
                                                        (-> element-props      :class mixed/to-vector))))

(defn effect-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:click-effect (keyword)(opt)
  ;  :disabled? (boolean)(opt)
  ;  :hover-effect (keyword)(opt)
  ;  :reveal-effect (keyword)(opt)}
  ;
  ; @usage
  ; (effect-attributes {...} {:click-effect :opacity :hover-effect :opacity})
  ; =>
  ; {:data-click-effect :opacity
  ;  :data-hover-effect :opacity
  ;  ...}
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-hover-effect (keyword)
  ;  :data-reveal-effect (keyword)}
  [element-attributes {:keys [click-effect disabled? hover-effect reveal-effect]}]
  ; Automatically turns off the hover and click effects on disabled elements.
  (if disabled? (map/merge-some element-attributes {:data-reveal-effect reveal-effect})
                (map/merge-some element-attributes {:data-click-effect  click-effect
                                                    :data-hover-effect  hover-effect
                                                    :data-reveal-effect reveal-effect})))

(defn link-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:href (string)(opt)
  ;  :target (keyword)(opt)
  ;   :blank, :self}
  ;
  ; @usage
  ; (link-attributes {...} {:href "/my-link" :target :blank})
  ; =>
  ; {:href   "/my-link"
  ;  :target :_blank
  ;  ...}
  ;
  ; @return (map)
  ; {:href (string)
  ;  :target (keyword)}
  [element-attributes {:keys [href target]}]
  (map/merge-some element-attributes {:href   href
                                      :target (case target :blank :_blank :self :_self nil)}))

(defn state-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:disabled? (boolean)(opt)}
  ;
  ; @usage
  ; (state-attributes {...} {:disabled? true})
  ; =>
  ; {:data-disabled true}
  ;
  ; @usage
  ; (state-attributes {...} {:disabled? true})
  ; =>
  ; {:data-disabled true
  ;  ...}
  ;
  ; @return (map)
  ; {:data-disabled (boolean)}
  [element-attributes {:keys [disabled?]}]
  (map/merge-some element-attributes {:data-disabled disabled?}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn badge-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:badge-color (keyword or string)
  ;  :badge-content (string)
  ;  :badge-position (keyword)}
  ;
  ; @usage
  ; (badge-attributes {...} {:badge-color :primary :badge-content "420" :badge-position :tr})
  ; =>
  ; {:data-badge-color    :primary
  ;  :data-badge-content  "420"
  ;  :data-badge-position :tr
  ;  ...}
  ;
  ; @return (map)
  ; {:data-badge-color (keyword)
  ;  :data-badge-content (string)
  ;  :data-badge-position (keyword)
  ;  :style (map)
  ;   {"--badge-color" (string)}}
  [element-attributes {:keys [badge-color badge-content badge-position]}]
  (-> (map/merge-some element-attributes {:data-badge-content  badge-content
                                          :data-badge-position badge-position})
      (utils/apply-property-value :badge-color :data-badge-color badge-color)))

(defn bubble-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:bubble-color (keyword or string)(opt)
  ;  :bubble-content (string)(opt)
  ;  :bubble-position (keyword)(opt)}
  ;
  ; @usage
  ; (bubble-attributes {...} {:bubble-color :primary :bubble-content "Hello bubble!" :bubble-position :left})
  ; =>
  ; {:data-bubble-color    :primary
  ;  :data-bubble-content  "Hello bubble!"
  ;  :data-bubble-position :left
  ;  ...}
  ;
  ; @return (map)
  ; {:data-bubble-color (keyword)
  ;  :data-bubble-content (string)
  ;  :data-bubble-position (keyword)
  ;  :style (map)
  ;   {"--bubble-color" (string)}}
  [element-attributes {:keys [bubble-color bubble-content bubble-position]}]
  (-> (map/merge-some element-attributes {:data-bubble-position bubble-content
                                          :data-bubble-content  bubble-position})
      (utils/apply-property-value :bubble-color :data-bubble-color bubble-color)))

(defn marker-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:marker-color (keyword or string)(opt)
  ;  :marker-position (keyword)(opt)}
  ;
  ; @usage
  ; (marker-attributes {...} {:marker-color :primary :marker-position :tr})
  ; =>
  ; {:data-marker-color    :primary
  ;  :data-marker-position :tr
  ;  ...}
  ;
  ; @return (map)
  ; {:data-marker-color (keyword)
  ;  :data-marker-position (keyword)
  ;  :style (map)
  ;   {"--marker-color" (string)}}
  [element-attributes {:keys [marker-color marker-position]}]
  (-> (map/merge-some element-attributes {:data-marker-position marker-position})
      (utils/apply-property-value :marker-color :data-marker-color marker-color)))

(defn progress-attributes
  ; @param (map) element-attributes
  ; {:style (map)(opt)}
  ; @param (map) element-props
  ; {:progress (percent)(opt)
  ;  :progress-color (keyword or string)(opt)
  ;  :progress-direction (keyword)(opt)
  ;  :progress-duration (ms)(opt)}
  ;
  ; @usage
  ; (progress-attributes {...} {:progress           50
  ;                             :progress-color     :primary
  ;                             :progress-direction :ltr
  ;                             :progress-duration  250})
  ; =>
  ; {:data-progress-color     :primary
  ;  :data-progress-direction :ltr
  ;  :style {"--progress"          "50%"
  ;          "--progress-duration" "250ms"}
  ;  ...}
  ;
  ; @return (map)
  ; {:data-progress-color (keyword)
  ;  :data-progress-direction (keyword)
  ;  :style (map)
  ;   {"--progress" (string)
  ;    "--progress-color" (string)
  ;    "--progress-duration" (string)}}
  [{:keys [style] :as element-attributes} {:keys [progress progress-color progress-direction progress-duration]}]
  (-> element-attributes (assoc :style (merge style (if progress          {"--progress"          (str progress          "%")})
                                                    (if progress-duration {"--progress-duration" (str progress-duration "ms")})))
                         (map/merge-some {:data-progress-direction progress-direction})
                         (utils/apply-property-value :progress-color :data-progress-color progress-color)))

(defn tooltip-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:tooltip-content (string)(opt)
  ;  :tooltip-position (keyword)(opt)}
  ;
  ; @usage
  ; (tooltip-attributes {...} {:tooltip-content "My tooltip" :tooltip-position :left})
  ; =>
  ; {:data-tooltip-content  "My tooltip"
  ;  :data-tooltip-position :left
  ;  ...}
  ;
  ; @return (map)
  ; {:data-tooltip-content (string)
  ;  :data-tooltip-position (keyword)}
  [element-attributes {:keys [tooltip-content tooltip-position]}]
  (map/merge-some element-attributes {:data-tooltip-content  tooltip-content
                                      :data-tooltip-position tooltip-position}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn mouse-event-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (function)(opt)
  ;  :on-mouse-down (function)(opt)
  ;  :on-mouse-over (function)(opt)
  ;  :on-mouse-up (function)(opt)}
  ;
  ; @usage
  ; (mouse-event-attributes {...} {:on-click (fn [e] ...)})
  ; =>
  ; {:on-click (fn [e] ...)
  ;  ...}
  ;
  ; @return (map)
  ; {:on-click (function)
  ;  :on-mouse-down (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)}
  [element-attributes {:keys [disabled? on-click on-mouse-down on-mouse-over on-mouse-up]}]
  (if disabled? (map/merge-some element-attributes {})
                (map/merge-some element-attributes {:on-click      on-click
                                                    :on-mouse-down on-mouse-down
                                                    :on-mouse-over on-mouse-over
                                                    :on-mouse-up   on-mouse-up})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-attributes
  ; @param (map) element-attributes
  ; @param (map) element-props
  ; {:disabled? (boolean)(opt)
  ;  :focus-id (keyword)(opt)}
  ;
  ; @usage
  ; (focus-attributes {...} {:focus-id :my-element})
  ; =>
  ; {:data-focus-id :my-element}
  ;
  ; @return (map)
  ; {:data-focus-id (keyword)}
  [element-attributes {:keys [disabled? focus-id]}]
  (if disabled? (map/merge-some element-attributes {})
                (map/merge-some element-attributes {:data-focus-id (hiccup/value focus-id)})))
