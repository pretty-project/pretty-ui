
(ns elements.label.prototypes
    (:require [hiccup.api              :as hiccup]
              [metamorphic-content.api :as metamorphic-content]
              [noop.api                :refer [param]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (map) label-props
  ; {:border-color (keyword or string)(opt)
  ;  :color (keyword)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :font-size (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :marker-color (keyword)
  ;  :target-id (keyword)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword)
  ;  :color (keyword or string)
  ;  :content (string)
  ;  :copyable? (boolean)
  ;  :font-size (keyword)
  ;  :font-weight (keyword)
  ;  :horizontal-align (keyword)
  ;  :icon-color (keyword)
  ;  :icon-family (keyword)
  ;  :icon-position (keyword)
  ;  :icon-size (keyword)
  ;  :line-height (keyword)
  ;  :marker-position (keyword)
  ;  :selectable? (boolean)
  ;  :target-id (string)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [{:keys [border-color color content font-size icon marker-color target-id tooltip-content] :as label-props}]
  ; XXX#7009
  ; The 'label-props-prototype' function applies the 'metamorphic-content/resolve' function
  ; on the 'content' value. Therefore no need to apply the 'metamorphic-content/resolve'
  ; function in multiple places (because it's already done in the prototype).
  (let [content (metamorphic-content/resolve content)]
       (merge {:font-size        :s
               :font-weight      :medium
               :horizontal-align :left
               :line-height      :text-block
               :selectable?      false}
              (if border-color    {:border-position :all
                                   :border-width    :xxs})
              (if marker-color    {:marker-position :tr})
              (if icon            {:icon-family :material-symbols-outlined
                                   :icon-color color :icon-size (or font-size :s)
                                   :icon-position :left})
              (if tooltip-content {:tooltip-position :right})
              (param label-props)
              {:content content}
              (if target-id        {:target-id       (hiccup/value target-id "input")})
              (if tooltip-content  {:tooltip-content (metamorphic-content/resolve tooltip-content)})
              (if (empty? content) {:copyable? false}))))
