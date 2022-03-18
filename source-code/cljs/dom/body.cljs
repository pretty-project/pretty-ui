
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.body)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-body-element
  ; @usage
  ;  (dom/get-body-element)
  ;
  ; @return (DOM-element)
  []
  (-> js/document (.getElementsByTagName "body")
                  (aget 0)))

                  

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-body-style-value
  ; @param (string) style-name
  ;
  ; @usage
  ;  (dom/get-element-style "background-color")
  ;
  ; @return (string)
  [style-name]
  (-> js/window (.getComputedStyle (-> js/document (.getElementsByTagName "body")
                                                   (aget 0)))
                (aget style-name)))
