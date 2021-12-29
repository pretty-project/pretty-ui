
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.06.21
; Description:
; Version: v0.6.2
; Compatibility: x4.4.6



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.listener
    (:require [mid-fruits.candy :refer [param return]]
              [x.app-core.api   :as a]
              [x.app-components.content :rename {component content}]))



;; -- Names -------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @name listener
;  A {:request-id ...} tulajdonságként átadott azonosítójú rendszerfolyamat
;  állapotától függően jeleníti meg a {:content {...}} vagy {:pending-content {...}}
;  tulajdonságként átadott tartalmat.
;
; @name infinite-listening?
;  A szerver válasz megérkezésekor a [listener] komponens ne térjen vissza
;  a {:content ...} tartalom rendereléséhez.
;  A request elküldése után a [listener] komponens a {:pending-content ...}
;  tartalmat rendereli.
;  Így elkerülhető, hogy a válasz megérkezésekor megtörténő események által
;  okozott UI-változások előtt újra felvillanjon a {:content ...} tartalom.
;
; @name {:listening? true}
;  A paraméterként átadott {:listening? true} tulajdonság kényszeríti
;  a [listener] komponenst, hogy a request állapotától függetlenül
;  :listening? állapotban legyen, és a {:pending-content ...} tartalmat renderelje.



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (metamorphic-content)
(def DEFAULT-PENDING-CONTENT "")

; @constant (metamorphic-content)
(def DEFAULT-FAILURE-CONTENT "")

; @constant (metamorphic-content)
(def DEFAULT-SUCCESS-CONTENT "")



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn extended-props->listener-props
  ; @param (map) extended-props
  ;
  ; @return (map)
  [extended-props]
  (select-keys extended-props [:content :content-props :failure-content :infinite-listening?
                               :listening? :pending-content :request-id :subscriber :success-content]))

(defn- context-props->listen-to-request?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context-props
  ;  {:listening? (boolean)(opt)
  ;   :request-id (keyword)(opt)}
  ;
  ; @return (boolean)
  [{:keys [listening? request-id]}]
  (or (some?   request-id)
      (boolean listening?)))

(defn- context-props->listening-to-request?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context-props
  ;  {:infinite-listening? (boolean)(opt)
  ;   :listening? (boolean)(opt)}
  ; @param (map) request-state
  ;  {:request-sent? (boolean)}
  ;
  ; @return (boolean)
  [{:keys [infinite-listening? listening?]} {:keys [request-sent?]}]
  (or (boolean listening?)
      (and     infinite-listening?
               request-sent?)))

(defn- request-state->context-props
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context-props
  ;  {:content (metamorphic-content)
  ;   :failure-content (metamorphic-content)(opt)
  ;   :listening? (boolean)(opt)
  ;   :pending-content (metamorphic-content)(opt)
  ;   :success-content (metamorphic-content)(opt)}
  ; @param (map) request-state
  ;  {:listening-to-request? (boolean)
  ;   :request-failured? (boolean)
  ;   :request-successed? (boolean)}
  ;
  ; @return (map)
  ;  {:base-props (map)
  ;    {:failured? (boolean)
  ;     :listening? (boolean)
  ;     :successed? (boolean)}
  ;   :content (metamorphic-content)}
  [{:keys [content failure-content pending-content listening? success-content] :as context-props}
   {:keys [listening-to-request? request-failured? request-successed?]         :as request-state}]
        ; A)
  (cond (context-props->listening-to-request? context-props request-state)
        (-> context-props (assoc    :content (or pending-content content))
                          (assoc-in [:base-props :listening?] true))
        ; B)
        (boolean listening-to-request?)
        (-> context-props (assoc    :content (or pending-content content))
                          (assoc-in [:base-props :listening?] true))
        ; C)
        (boolean request-failured?)
        (-> context-props (assoc    :content (or failure-content content))
                          (assoc-in [:base-props :failured?] true))
        ; D)
        (boolean request-successed?)
        (-> context-props (assoc    :content (or success-content content))
                          (assoc-in [:base-props :successed?] true))
        ; E)
        :else (return context-props)))



;; -- Prototypes --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- context-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (map) context-props
  ;  {:failure-content (metamorphic-content)(opt)
  ;   :pending-content (metamorphic-content)(opt)
  ;   :request-id (keyword)(opt)
  ;   :success-content (metamorphic-content)(opt)}
  ;
  ; @return (component)
  ; {:failure-content (metamorphic-content)
  ;  :pending-content (metamorphic-content)
  ;  :success-content (metamorphic-content)
  [{:keys [failure-content pending-content request-id success-content] :as context-props}]
  (merge {}
         (param context-props)
         ; Use default {:failure-content ...}
         (if (and (some? request-id) (= failure-content :use-default!))
             {:failure-content DEFAULT-FAILURE-CONTENT})
         ; Use default {:pending-content ...}
         (if (and (some? request-id) (= pending-content :use-default!))
             {:pending-content DEFAULT-PENDING-CONTENT})
         ; Use default {:success-content ...}
         (if (and (some? request-id) (= success-content :use-default!))
             {:success-content DEFAULT-SUCCESS-CONTENT})))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- listener
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) context-props
  ;  {:request-id (keyword)}
  ;
  ; @return (component)
  [component-id {:keys [request-id] :as context-props}]
  (let [request-state (a/subscribe [:sync/get-request-state request-id])]
       (fn [_ context-props]
           (let [context-props (request-state->context-props context-props @request-state)]
                [content component-id context-props]))))

(defn component
  ; @param (keyword)(opt) component-id
  ; @param (map) context-props
  ;  {:content (metamorphic-content)
  ;    XXX#8711
  ;   :content-props (map)(opt)
  ;    XXX#8711
  ;   :failure-content (metamorphic-content)(opt)
  ;    {:failure-content :use-default!}
  ;    => {:failure-content DEFAULT-FAILURE-CONTENT}
  ;   :infinite-listening? (boolean)(opt)
  ;    Default: false
  ;   :listening? (boolean)(opt)
  ;    Default: false
  ;   :pending-content (metamorphic-content)(opt)
  ;    {:pending-content :use-default!}
  ;    => {:pending-content DEFAULT-PENDING-CONTENT}
  ;   :request-id (keyword)(opt)
  ;   :subscriber (subscription-vector)(opt)
  ;    Return value must be a map!
  ;    XXX#8711
  ;   :success-content (metamorphic-content)(opt)
  ;    {:success-content :use-default!}
  ;    => {:success-content DEFAULT-SUCCESS-CONTENT}}
  ;
  ; @usage
  ;  [components/listener {...}]
  ;
  ; @usage
  ;  [components/listener :my-listener {...}]
  ;
  ; @usage
  ;  (defn my-component       [component-id dynamic-props])
  ;  (defn my-ghost-component [component-id dynamic-props])
  ;  [components/listener {:content         #'my-component
  ;                        :pending-content #'my-ghost-component
  ;                        :request-id      :my-request
  ;                        :subscriber      [::get-view-props]}]
  ;
  ; @usage
  ;  (defn my-component       [component-id static-props dynamic-props])
  ;  (defn my-ghost-component [component-id static-props dynamic-props])
  ;  [components/listener {:content         #'my-component
  ;                        :pending-content #'my-ghost-component
  ;                        :request-id      :my-request
  ;                        :static-props    {...}
  ;                        :subscriber      [::get-view-props]}]
  ;
  ; @usage
  ;  (defn my-component [component-id {:keys [failured? listening? successed?]}])
  ;  [components/listener {:content         #'my-component
  ;                        :request-id      :my-request}]
  ;
  ; @usage
  ;  (defn my-component [_ _])
  ;  [components/listener {:content         #'my-component
  ;                        :failure-content :use-default!
  ;                        :pending-content :use-default!
  ;                        :request-id      :my-request
  ;                        :success-content :use-default!}]
  ;
  ; @return (component)
  ([context-props]
   [component (a/id) context-props])

  ([component-id context-props]
   (let [context-props (context-props-prototype context-props)]
        (if (context-props->listen-to-request? context-props)
            [listener component-id context-props]
            [content  component-id context-props]))))
