
(ns pretty-elements.label.prototypes
    (:require [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api    :as pretty-build-kit]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (map) label-props
  ; {:border-color (keyword or string)(opt)
  ;  :font-size (keyword, px or string)(opt)
  ;  :icon (keyword)(opt)
  ;  :marker-color (keyword or string)
  ;  :text-color (keyword or string)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword, px or string)
  ;  :copyable? (boolean)
  ;  :font-size (keyword, px or string)
  ;  :font-weight (keyword or integer)
  ;  :horizontal-align (keyword)
  ;  :icon-color (keyword or string)
  ;  :icon-family (keyword)
  ;  :icon-position (keyword)
  ;  :icon-size (keyword, px or string)
  ;  :line-height (keyword, px or string)
  ;  :marker-position (keyword)
  ;  :placeholder (metamorphic-content)
  ;  :selectable? (boolean)
  ;  :text-color (keyword or string)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [{:keys [border-color font-size icon marker-color text-color tooltip-content] :as label-props}]
  ; BUG#9811
  ; - In some cases the content can be an empty string for a short while before it
  ;   gets its value (e.g., from a subscription or a HTTP request, etc.).
  ;   Therefore, the placeholder has to get the same height, even if it's empty!
  ;   Otherwise, an empty placeholder, and a delayed content would cause a short
  ;   flickering by the inconsistent label height!
  ; - Solution:
  ;   In case of the placeholder is also an empty string, the "\u00A0" white
  ;   character provides the consistent height for the element until the content gets its value.
  (merge {:font-size        :s
          :font-weight      :medium
          :line-height      :text-block
          :placeholder      "\u00A0"
          :selectable?      false}
         (if border-color    {:border-position :all
                              :border-width    :xxs})
         (if marker-color    {:marker-position :tr})
         (if icon            {:icon-family :material-symbols-outlined
                              :icon-color text-color :icon-size (or font-size :s)
                              :icon-position :left})
         (if tooltip-content {:tooltip-position :right})
         (-> label-props)
         (if tooltip-content {:tooltip-content (metamorphic-content/compose tooltip-content)})))
