
(ns elements.text.views
    (:require [elements.element.views   :as element.views]
              [elements.text.attributes :as text.attributes]
              [elements.text.prototypes :as text.prototypes]
              [metamorphic-content.api  :as metamorphic-content]
              [noop.api                 :refer [return]]
              [random.api               :as random]
              [string.api               :as string]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-placeholder
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:placeholder (metamorphic-content)(opt)}
  [_ {:keys [placeholder]}]
  ; BUG#9811 (source-code/cljs/elements/label/views.cljs)
  [:div {:class           :e-text--placeholder
         :data-selectable false}
        (if placeholder (metamorphic-content/compose placeholder)
                        "\u00A0")])

(defn- text-content-rows
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:content (metamorphic-content)}
  [_ {:keys [content]}]
  ; XXX#7009 (source-code/cljs/elements/label/prototypes.cljs)
  ;
  ; If the content is string (it might be hiccup), splits it into HTML rows by
  ; replacing newline characters ("\n") with [:br] tags.
  (letfn [(f [%1 %2 %3] (if (= 0 %2) (conj %1       %3)
                                     (conj %1 [:br] %3)))]
         (if (string? content)
             (let [content-rows (string/split content "\n")]
                  (reduce-kv f [:<>] content-rows))
             (return content))))

(defn- text-content
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:on-copy (Re-Frame metamorphic-event)(opt)}
  [text-id {:keys [on-copy] :as text-props}]
  (if on-copy [:div (text.attributes/copyable-attributes text-id text-props)
                    [:div (text.attributes/content-attributes text-id text-props)
                          (text-content-rows                  text-id text-props)]]
              [:<>  [:div (text.attributes/content-attributes text-id text-props)
                          (text-content-rows                  text-id text-props)]]))

(defn- text
  ; @ignore
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:content (string)}
  [text-id {:keys [content] :as text-props}]
  ; XXX#7009 (source-code/cljs/elements/label/prototypes.cljs)
  [:div (text.attributes/text-attributes text-id text-props)
        [element.views/element-label  text-id text-props]
        [:div (text.attributes/text-body-attributes text-id text-props)
              (if (empty? content)
                  [text-placeholder text-id text-props]
                  [text-content     text-id text-props])]])

(defn element
  ; @param (keyword)(opt) text-id
  ; @param (map) text-props
  ; {:class (keyword or keywords in vector)(opt)
  ;  :color (keyword or string)(opt)
  ;   :default, :highlight, :inherit, :invert, :muted, :primary, :secondary, :success, :warning
  ;   Default: :default
  ;  :content (metamorphic-content)(opt)
  ;  :font-size (keyword)(opt)
  ;   :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl, :inherit
  ;   Default: :s
  ;  :font-weight (keyword)(opt)
  ;   :inherit, :thin, :extra-light, :light, :normal, :medium, :semi-bold, :bold, :extra-bold, :black
  ;   Default: :normal
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :left
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
  ;  :line-height (keyword)(opt)
  ;   :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :max-lines (integer)(opt)
  ;  :on-copy (Re-Frame metamorphic-event)(opt)
  ;   This event takes the text content as its last parameter
  ;  :outdent (map)(opt)
  ;   Same as the :indent property
  ;  :placeholder (metamorphic-content)(opt)
  ;  :selectable? (boolean)(opt)
  ;   Default: true
  ;  :style (map)(opt)
  ;  :text-direction (keyword)(opt)
  ;   :normal, :reversed
  ;   Default :normal
  ;  :text-overflow (keyword)(opt)
  ;   :ellipsis, :wrap
  ;   Default: :wrap}
  ;
  ; @usage
  ; [text {...}]
  ;
  ; @usage
  ; [text :my-text {...}]
  ([text-props]
   [element (random/generate-keyword) text-props])

  ([text-id text-props]
   (let [text-props (text.prototypes/text-props-prototype text-props)]
        [text text-id text-props])))
