

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.element)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-by-id
  ; @param (string) element-id
  ; @param (DOM element)(opt) parent-element
  ;  Default: js/document
  ;
  ; @usage
  ;  (dom/get-element-by-id "my-element")
  ;
  ; @return (DOM-element or nil)
  ([element-id]                (.getElementById js/document    element-id))
  ([element-id parent-element] (.getElementById parent-element element-id)))

(defn get-elements-by-query-selector
  ; @param (DOM-element) parent-element
  ; @param (string) query-selector
  ;  XXX#7603
  ;
  ; @usage
  ;  (dom/get-elements-by-query-selector head-element "[type=\"text/css\"]")
  ;
  ; @usage
  ;  (dom/get-elements-by-query-selector body-element "div.my-class, div.your-class")
  ;
  ; @return (vector)
  [parent-element query-selector]
  (-> parent-element (.querySelectorAll query-selector) array-seq vec))

(defn get-elements-by-class-name
  ; @param (string) class-name
  ; @param (DOM element)(opt) parent-element
  ;  Default: js/document
  ;
  ; @usage
  ;  (dom/get-elements-by-class-name "my-class")
  ;
  ; @return (vector)
  ([class-name]                (-> js/document    (.getElementsByClassName class-name) array-seq vec))
  ([class-name parent-element] (-> parent-element (.getElementsByClassName class-name) array-seq vec)))

(defn get-elements-by-tag-name
  ; @param (string) tag-name
  ; @param (DOM element)(opt) parent-element
  ;  Default: js/document
  ;
  ; @usage
  ;  (dom/get-elements-by-class-name "div")
  ;
  ; @return (vector)
  ([tag-name]                (-> js/document    (.getElementsByTagName tag-name) array-seq vec))
  ([tag-name parent-element] (-> parent-element (.getElementsByTagName tag-name) array-seq vec)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-disabled?
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/element-disabled? my-element)
  ;
  ; @return (boolean)
  [element]
  (-> element .-disabled boolean))

(defn element-enabled?
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/element-enabled? my-element)
  ;
  ; @return (boolean)
  [element]
  (-> element .-disabled not))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-element-id!
  ; @param (DOM-element) element
  ; @param (string) element-id
  ;
  ; @usage
  ;  (dom/set-element-id! my-element "my-element-id")
  [element element-id]
  (-> element .-id (set! element-id)))
