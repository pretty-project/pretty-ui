
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.document)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-document-element
  ; @usage
  ;  (dom/get-document-element)
  ;
  ; @return (DOM-element)
  []
  (.-documentElement js/document))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-document-height
  ; @usage
  ;  (dom/get-document-height)
  ;
  ; @return (px)
  []
  (-> js/document .-documentElement .-scrollHeight))

(defn get-document-width
  ; @usage
  ;  (dom/get-document-width)
  ;
  ; @return (px)
  []
  (-> js/document .-documentElement .-scrollWidth))
