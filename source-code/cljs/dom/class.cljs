
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.class)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-element-class!
  ; @param (DOM-element) element
  ; @param (string) class-name
  ;
  ; @usage
  ;  (dom/set-element-class! my-element "my-class")
  [element class-name]
  (-> element .-classList (.add class-name)))

(defn remove-element-class!
  ; @param (DOM-element) element
  ; @param (string) class-name
  ;
  ; @usage
  ;  (dom/remove-element-class! my-element "my-class")
  [element class-name]
  (-> element .-classList (.remove class-name)))
