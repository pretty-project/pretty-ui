
(ns pretty-build-kit.adaptive)

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn adaptive-border-radius
  ; @param (keyword, px or string) border-radius
  ; @param (number) ratio
  ;
  ; @usage
  ; (adaptive-border-radius :s 0.3)
  ; =>
  ; "calc(var(--border-radius-s) * 0.3)"
  ;
  ; @usage
  ; (adaptive-border-radius "5%" 0.3)
  ; =>
  ; "calc(5% * 0.3)"
  ;
  ; @usage
  ; (adaptive-border-radius 10 0.3)
  ; =>
  ; "calc(10px * 0.3)"
  ;
  ; @usage
  ; (adaptive-border-radius nil 0.3)
  ; =>
  ; nil
  ;
  ; @return (string)
  [border-radius ratio]
  ; Using an adaptive border radius makes the element's border track the curve of an inner or outer element's border.
  (cond (keyword? border-radius) (str "calc(var(--border-radius-" (name border-radius) ") * " ratio ")")
        (string?  border-radius) (str "calc(" border-radius   " * " ratio ")")
        (integer? border-radius) (str "calc(" border-radius "px * " ratio ")")))

(defn adaptive-text-height
  ; @description
  ; - Returns a CSS 'calc' function that calculates the height of multiple lines.
  ; - The 'line-height' value can be ...
  ;   ... a predefined line-height profile as a keyword (e.g., :micro, :xxs, :xs, etc.).
  ;   ... a predefined line-height variable as a keyword (:text-block or :auto).
  ;   ... a px value as an integer (e.g., 42 => 42px).
  ;   ... a CSS unit as a string (e.g., "42px", "3em", etc.).
  ;
  ; @param (keyword, px or string) font-size
  ; @param (keyword, px or string) line-height
  ; @param (integer) line-count
  ; @param (px)(opt) horizontal-indent
  ;
  ; @usage
  ; (adaptive-text-height :s :text-block 3)
  ; =>
  ; "calc(var(--text-block-s) * 3)"
  ;
  ; @usage
  ; (adaptive-text-height :s :auto 3)
  ; =>
  ; "calc(var(--line-height-s) * 3)"
  ;
  ; @usage
  ; (adaptive-text-height "12px" :auto 3)
  ; =>
  ; "calc(12px * 1.5 * 3)"
  ;
  ; @usage
  ; (adaptive-text-height ... :s 3)
  ; =>
  ; "calc(var(--line-height-s) * 3)"
  ;
  ; @usage
  ; (adaptive-text-height ... "18px" 3)
  ; =>
  ; "calc(18px * 3)"
  ;
  ; @usage
  ; (adaptive-text-height "..." "..." ... 24)
  ; =>
  ; "calc(... + 24px)"
  ;
  ; @return (string)
  [font-size line-height line-count & [horizontal-indent]]
  ; According to the 'pretty-css/presets/font.css' stylesheet, the default line-height/font-size ratio is 1.5.
  (cond (integer? font-size)         (adaptive-text-height (str font-size "px") line-height line-count horizontal-indent)       ; <- Appends "px" suffix to integer type 'font-size' values.
        (integer? line-height)       (adaptive-text-height font-size (str line-height "px") line-count horizontal-indent)       ; <- Appends "px" suffix to integer type 'line-height' values.
        (integer? horizontal-indent) (adaptive-text-height font-size line-height line-count (str " + " horizontal-indent "px")) ; <- Wraps integer type 'horizontal-indent' values with " + " and  "px".
        (and (= line-height :text-block) (string?  font-size)) (-> nil) ; Text-block line-heights only available for predefined font-size profiles.
        (and (= line-height :text-block) (keyword? font-size)) (str "calc(var(--text-block-height-" (name font-size)   ") * " line-count horizontal-indent ")")
        (and (= line-height :auto)       (keyword? font-size)) (str "calc(var(--line-height-"       (name font-size)   ") * " line-count horizontal-indent ")")
        (and (= line-height :auto)       (string?  font-size)) (str "calc(" font-size " * 1.5 * "                             line-count horizontal-indent ")")
        (and (keyword? line-height))                           (str "calc(var(--line-height-"       (name line-height) ") * " line-count horizontal-indent ")")
        (and (string?  line-height))                           (str "calc("line-height" * "                                   line-count horizontal-indent ")")))
