
(ns pretty-build-kit.utils)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn apply-property-value
  ; @ignore
  ;
  ; @description
  ; - Applies the given multi-type CSS property value on the given element attributes map.
  ; - If the given value is provided as ...
  ;   ... a string, associates it with the given CSS variable name into the style map within the given element attributes map.
  ;   ... an integer, associates it with the given CSS variable name into the style map within the given element attributes map.
  ;   ... a keyword, associates it with the given data attribute name into the given element attributes map.
  ; - Integer type values are suffixed with the given 'default-unit' value.
  ;
  ; @param (map) element-attributes
  ; {:style (map)(opt)}
  ; @param (keyword) css-var-name
  ; @param (keyword) data-attribute-name
  ; @param (keyword or string) property-value
  ; @param (string)(opt) default-unit
  ; Appended to the given value in case it is provided as an integer.
  ;
  ; @usage
  ; (apply-property-value {...} :text-color :data-text-color :muted)
  ; =>
  ; {:data-text-color :muted
  ;  ...}
  ;
  ; @usage
  ; (apply-property-value {...} :text-color :data-text-color "#fff")
  ; =>
  ; {:data-text-color :var :style {"--text-color" "#fff"}
  ;  ...}
  ;
  ; @usage
  ; (apply-property-value {:style {:padding "12px"} ...} :text-color :data-text-color "#fff")
  ; =>
  ; {:data-text-color :var :style {"--text-color" "#fff" :padding "12px"}
  ;  ...}
  ;
  ; @usage
  ; (apply-property-value {...} :element-width :data-element-width 420 "px")
  ; =>
  ; {:data-element-width :var :style {"--element-width" "420px"}
  ;  ...}
  ;
  ; @return (map)
  ; {:style (map)}
  [element-attributes css-var-name data-attribute-name property-value & [default-unit]]
  ; By using a CSS variable to address the given value, not just the element but its pseudo-elements can use it.
  (cond (integer? property-value) (-> element-attributes (apply-property-value css-var-name data-attribute-name (str property-value default-unit)))
        (keyword? property-value) (-> element-attributes (assoc-in [data-attribute-name] property-value))
        (string?  property-value) (-> element-attributes (assoc-in [:style (str "--" (name css-var-name))] property-value)
                                                         (assoc-in [data-attribute-name] :var))
        :return element-attributes))
