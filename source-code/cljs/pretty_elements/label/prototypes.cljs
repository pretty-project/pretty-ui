
(ns pretty-elements.label.prototypes
    (:require [fruits.hiccup.api       :as hiccup]
              [metamorphic-content.api :as metamorphic-content]
              [pretty-build-kit.api :as pretty-build-kit]
              [fruits.noop.api :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; @ignore
  ;
  ; @param (map) label-props
  ; {:border-color (keyword or string)(opt)
  ;  :font-size (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :marker-color (keyword)
  ;  :text-color (keyword)(opt)
  ;  :target-id (keyword)(opt)
  ;  :tooltip-content (metamorphic-content)(opt)}
  ;
  ; @return (map)
  ; {:border-position (keyword)
  ;  :border-width (keyword)
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
  ;  :placeholder (metamorphic-content)
  ;  :selectable? (boolean)
  ;  :target-id (string)
  ;  :text-color (keyword or string)
  ;  :tooltip-content (string)
  ;  :tooltip-position (keyword)}
  [{:keys [border-color font-size icon marker-color target-id text-color tooltip-content] :as label-props}]
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
          :horizontal-align :left
          :line-height      :text-block
          :placeholder      "\u00A0"
          :selectable?      false
          :content-value-f return
          :placeholder-value-f return}
         (if border-color    {:border-position :all
                              :border-width    :xxs})
         (if marker-color    {:marker-position :tr})
         (if icon            {:icon-family :material-symbols-outlined
                              :icon-color text-color :icon-size (or font-size :s)
                              :icon-position :left})
         (if tooltip-content {:tooltip-position :right})
         (-> label-props)
         (if target-id        {:target-id       (hiccup/value target-id "input")})
         (if tooltip-content  {:tooltip-content (metamorphic-content/compose tooltip-content)})))
