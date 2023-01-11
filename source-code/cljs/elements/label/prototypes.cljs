
(ns elements.label.prototypes
    (:require [candy.api        :refer [param]]
              [hiccup.api       :as hiccup]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) label-props
  ; {:color (keyword)(opt)
  ;  :content (metamorphic-content)(opt)
  ;  :font-size (keyword)(opt)
  ;  :icon (keyword)(opt)
  ;  :marker-color (keyword)
  ;  :target-id (keyword)(opt)}
  ;
  ; @return (map)
  ; {:color (keyword or string)
  ;  :content (string)
  ;  :copyable? (boolean)
  ;  :font-size (keyword)
  ;  :font-weight (keyword)
  ;  :horizontal-align (keyword)
  ;  :icon-color (keyword)
  ;  :icon-family (keyword)
  ;  :icon-size (keyword)
  ;  :line-height (keyword)
  ;  :marker-position (keyword)
  ;  :selectable? (boolean)
  ;  :target-id (string)
  ;  :text-overflow (keyword)}
  [{:keys [color content font-size icon marker-color target-id] :as label-props}]
  ; XXX#7009
  ; The 'label-props-prototype' function applies the 'x.components/content' function
  ; on the 'content' value. Therefore no need to apply the 'x.components/content'
  ; function in multiple places.
  (let [content (x.components/content content)]
       (merge {:font-size        :s
               :font-weight      :bold
               :horizontal-align :left
               :line-height      :text-block
               :selectable?      false
               :text-overflow   :ellipsis}
              (if marker-color {:marker-position :tr})
              (if icon         {:icon-family :material-icons-filled
                                :icon-color color :icon-size (or font-size :s)})
              (param label-props)
              {:content content}
              (if target-id        {:target-id (hiccup/value target-id "input")})
              (if (empty? content) {:copyable? false}))))
