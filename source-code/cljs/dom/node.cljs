
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns dom.node)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-before!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ; @param (DOM-element) after-element
  ;
  ; @usage
  ;  (dom/insert-before! my-parent-element my-child-element my-after-element)
  [parent-element child-element after-element]
  (.insertBefore parent-element child-element after-element))

(defn insert-after!
  ; https://www.javascripttutorial.net/javascript-dom/javascript-insertafter/
  ;
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ; @param (DOM-element) before-element
  ;
  ; @usage
  ;  (dom/insert-after! my-parent-element my-child-element my-before-element)
  [parent-element child-element before-element]
  (.insertBefore parent-element child-element (.-nextSibling before-element)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-as-first-of-type!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ;
  ; @usage
  ;  (dom/insert-as-first-of-type! my-parent-element my-child-element)
  [parent-element child-element]
  (.insertBefore parent-element child-element
                 (-> parent-element (.getElementsByTagName (-> child-element .-tagName))
                     array-seq first)))

(defn insert-as-last-of-type!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ;
  ; @usage
  ;  (dom/insert-as-last-of-type! my-parent-element my-child-element)
  [parent-element child-element]
  (.insertBefore parent-element child-element
                 (-> parent-element (.getElementsByTagName (-> child-element .-tagName))
                     array-seq last .-nextSibling)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-as-first-of-query-selected!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ; @param (string) query-selector
  ;  XXX#7603
  ;
  ; @usage
  ;  (dom/insert-as-first-of-query-selected! head-element link-element "[type=\"text/css\"]")
  ;
  ; @usage
  ;  (dom/insert-as-first-of-query-selected! body-element my-element "div.my-class, div.your-class")
  [parent-element child-element query-selector]
  (.insertBefore parent-element child-element
                 (-> parent-element (.querySelectorAll query-selector) array-seq first)))

(defn insert-as-last-of-query-selected!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ; @param (string) query-selector
  ;  XXX#7603
  ;
  ; @usage
  ;  (dom/insert-as-first-of-query-selected! head-element link-element "[type=\"text/css\"]")
  ;
  ; @usage
  ;  (dom/insert-as-first-of-query-selected! body-element my-element "div.my-class, div.your-class")
  [parent-element child-element query-selector]
  (.insertBefore parent-element child-element
                 (-> parent-element (.querySelectorAll query-selector) array-seq last .-nextSibling)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn append-element!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ;
  ; @usage
  ;  (dom/append-element! my-parent-element my-child-element)
  [parent-element child-element]
  (.appendChild parent-element child-element))

(defn prepend-element!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ;
  ; @usage
  ;  (dom/prepend-element! my-parent-element my-child-element)
  [parent-element child-element]
  (.insertBefore parent-element child-element (.-firstChild parent-element)))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn create-element!
  ; @param (string) nodename
  ;
  ; @usage
  ;  (dom/create-element! "div")
  ;
  ; @return (DOM-element)
  [nodename]
  (.createElement js/document nodename))

(defn remove-element!
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/remove-element! my-element)
  [element]
  (.remove element))

(defn remove-child!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ;
  ; @usage
  ;  (dom/remove-child! my-parent-element my-child-element)
  [parent-element child-element]
  (.removeChild parent-element child-element))

  

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-element!
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/empty-element! my-element)
  [element]
  (while (.-firstChild element)
         (.removeChild element (.-firstChild element))))

(defn set-element-content!
  ; @param (DOM-element) element
  ; @param (string) content
  ;
  ; @usage
  ;  (dom/set-element-content! my-element "Hakuna Matata!")
  ;
  ; @return (string)
  [element content]
  (-> element .-innerHTML (set! content)))
