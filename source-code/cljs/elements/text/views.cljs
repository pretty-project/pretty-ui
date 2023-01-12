
(ns elements.text.views
    (:require [candy.api                :refer [return]]
              [elements.element.views   :as element.views]
              [elements.text.helpers    :as text.helpers]
              [elements.text.prototypes :as text.prototypes]
              [random.api               :as random]
              [string.api               :as string]
              [x.components.api         :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- text-placeholder
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:placeholder (metamorphic-content)(opt)}
  [_ {:keys [placeholder]}]
  ; BUG#9811 (source-code/cljs/elements/label/views.cljs)
  [:div.e-text--placeholder {:data-selectable false}
                            (if placeholder (x.components/content placeholder)
                                            "\u00A0")])

(defn- text-content-rows
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:content (metamorphic-content)}
  [_ {:keys [content]}]
  ; XXX#7009 (source-code/cljs/elements/label/prototypes.cljs)
  (letfn [(f [%1 %2 %3] (if (= 0 %2) (conj %1       %3)
                                     (conj %1 [:br] %3)))]
         (if (string? content)
             (let [content-rows (string/split content "\n")]
                  (reduce-kv f [:<>] content-rows))
             ; A content értéke nem kizárólag string típus lehet (pl. hiccup, ...)
             (return content))))

(defn- text-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:copyable? (boolean)(opt)}
  [text-id {:keys [copyable?] :as text-props}]
  (if copyable? [:div.e-text--copyable (text.helpers/copyable-attributes text-id text-props)
                                       [:div.e-text--content (text.helpers/content-attributes text-id text-props)
                                                             (text-content-rows               text-id text-props)]]
                [:<>                   [:div.e-text--content (text.helpers/content-attributes text-id text-props)
                                                             (text-content-rows               text-id text-props)]]))

(defn- text-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  ; {:content (string)
  ;  :placeholder (metamorphic-content)(opt)}
  [text-id {:keys [content] :as text-props}]
  ; XXX#7009 (source-code/cljs/elements/label/prototypes.cljs)
  [:div.e-text--body (text.helpers/text-body-attributes text-id text-props)
                     (if (empty? content)
                         [text-placeholder text-id text-props]
                         [text-content     text-id text-props])])

(defn- text
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) text-id
  ; @param (map) text-props
  [text-id text-props]
  [:div.e-text (text.helpers/text-attributes text-id text-props)
               [element.views/element-label  text-id text-props]
               [text-body                    text-id text-props]])

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
  ;   :inherit, :extra-light, :light, :normal, :medium, :bold, :extra-bold
  ;   Default: :normal
  ;  :horizontal-align (keyword)(opt)
  ;   :center, :left, :right
  ;   Default: :left
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
  ;  :line-height (keyword)(opt)
  ;   :inherit, :native, :text-block, :xxs, :xs, :s, :m, :l, :xl, :xxl, :3xl, :4xl, :5xl
  ;   Default: :text-block
  ;  :max-lines (integer)(opt)
  ;  :outdent (map)(opt)
  ;   Same as the :indent property.
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
