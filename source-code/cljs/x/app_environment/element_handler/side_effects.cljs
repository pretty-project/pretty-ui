
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-environment.element-handler.side-effects
    (:require [app-fruits.dom :as dom]
              [x.app-core.api :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-disabled?
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/element-disabled? "my-element")
  ;
  ; @return (boolean)
  [element-id]
  (boolean (if-let [element (dom/get-element-by-id element-id)]
                   (dom/element-disabled? element))))

(defn element-enabled?
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/element-enabled? "my-element")
  ;
  ; @return (boolean)
  [element-id]
  (boolean (if-let [element (dom/get-element-by-id element-id)]
                   (dom/element-enabled? element))))



;; -- Focus/blur side-effect events -------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-element!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/focus-element! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/focus-element! element)))

; @usage
;  [:environment/focus-element! "my-element"]
(a/reg-fx :environment/focus-element! focus-element!)

(defn blur-element!
  ; @param (string)(opt) element-id
  ;
  ; @usage
  ;  (environment/blur-element! "my-element")
  []
  (if-let [element (dom/get-active-element)]
          (dom/blur-element! element)))

; @usage
;  [:environment/blur-element!]
(a/reg-fx :environment/blur-element! blur-element!)



;; -- Classlist side-effect events --------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-element-class!
  ; @param (string) element-id
  ; @param (string) class-name
  ;
  ; @usage
  ;  (environment/add-element-class! "my-element" "my-class")
  [element-id class-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-class! element class-name)))

; @usage
;  [:environment/add-element-class! "my-element" "my-class"]
(a/reg-fx :environment/add-element-class! add-element-class!)

(defn remove-element-class!
  ; @param (string) element-id
  ; @param (string) class-name
  ;
  ; @usage
  ;  (environment/remove-element-class! "my-element" "my-class")
  [element-id class-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-class! element class-name)))

; @usage
;  [:environment/remove-element-class! "my-element" "my-class"]
(a/reg-fx :environment/remove-element-class! remove-element-class!)



;; -- Attribute side-effect events --------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-element-style!
  ; @param (string) element-id
  ; @param (map) style
  ;
  ; @usage
  ;  (environment/set-element-style! "my-element" {:opacity "1"})
  [element-id style]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-style! element style)))

; @usage
;  [:environment/set-element-style! "my-element" {:opacity "1"}]
(a/reg-fx :environment/set-element-style! set-element-style!)

(defn remove-element-style!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/remove-element-style! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-style! element)))

; @usage
;  [:environment/remove-element-style! "my-element"]
(a/reg-fx :environment/remove-element-style! remove-element-style!)

(defn set-element-style-value!
  ; @param (string) element-id
  ; @param (string) style-name
  ; @param (string) style-value
  ;
  ; @usage
  ;  (environment/set-element-style-value! "my-element" "opacity" "1")
  [element-id style-name style-value]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-style-value! element style-name style-value)))

; @usage
;  [:environment/set-element-style-value! "my-element" "opacity" "1"]
(a/reg-fx :environment/set-element-style-value! set-element-style-value!)

(defn remove-element-style-value!
  ; @param (string) element-id
  ; @param (string) style-name
  ;
  ; @usage
  ;  (environment/remove-element-style-value! "my-element" "opacity")
  [element-id style-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-style-value! element style-name)))

; @usage
;  [:environment/remove-element-style-value! "my-element" "opacity"]
(a/reg-fx :environment/remove-element-style-value! remove-element-style-value!)

(defn set-element-attribute!
  ; @param (string) element-id
  ; @param (string) attribute-name
  ; @param (*) attribute-value
  ;
  ; @usage
  ;  (environment/set-element-attribute! "my-element" "my-attribute" "my-value")
  [element-id attribute-name attribute-value]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/set-element-attribute! element attribute-name attribute-value)))

; @usage
;  [:environment/set-element-attribute! "my-element" "my-attribute" "my-value"]
(a/reg-fx :environment/set-element-attribute! set-element-attribute!)

(defn remove-element-attribute!
  ; @param (string) element-id
  ; @param (string) attribute-name
  ;
  ; @usage
  ;  (environment/remove-element-attribute! "my-element" "my-attribute")
  [element-id attribute-name]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element-attribute! element attribute-name)))

; @usage
;  [:environment/remove-element-attribute! "my-element" "my-attribute"]
(a/reg-fx :environment/remove-element-attribute! remove-element-attribute!)



;; -- Node side-effect events -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn empty-element!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/empty-element! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/empty-element! element)))

; @usage
;  [:environment/empty-element! "my-element"]
(a/reg-fx :environment/empty-element! empty-element!)

(defn remove-element!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/remove-element! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (dom/remove-element! element)))

; @usage
;  [:environment/remove-element! "my-element"]
(a/reg-fx :environment/remove-element! remove-element!)



;; -- Visibility side-effect events -------------------------------------------
;; ----------------------------------------------------------------------------

(defn reveal-element!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/reveal-element! "my-element")
  [element-id]
  (set-element-style-value! element-id "display" "block"))

; @usage
;  [:environment/reveal-element! "my-element"]
(a/reg-fx :environment/reveal-element! reveal-element!)

(defn hide-element!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/hide-element! "my-element")
  [element-id]
  (set-element-style-value! element-id "display" "none"))

; @usage
;  [:environment/hide-element! "my-element"]
(a/reg-fx :environment/hide-element! hide-element!)



;; -- Masspoint side-effect events --------------------------------------------
;; ----------------------------------------------------------------------------

(defn mark-element-masspoint-orientation!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/mark-element-masspoint-orientation! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (let [masspoint-orientation (dom/get-element-masspoint-orientation element)]
               (set-element-attribute! element-id "data-masspoint-orientation" (a/dom-value masspoint-orientation)))))

; @usage
;  [:environment/mark-element-masspoint-orientation! "my-element"]
(a/reg-fx :environment/mark-element-masspoint-orientation! mark-element-masspoint-orientation!)

(defn unmark-element-masspoint-orientation!
  ; @param (string) element-id
  ;
  ; @usage
  ;  (environment/unmark-element-masspoint-orientation! "my-element")
  [element-id]
  (if-let [element (dom/get-element-by-id element-id)]
          (remove-element-attribute! element-id "data-masspoint-orientation")))

; @usage
;  [:environment/unmark-element-masspoint-orientation! "my-element"]
(a/reg-fx :environment/unmark-element-masspoint-orientation! unmark-element-masspoint-orientation!)
