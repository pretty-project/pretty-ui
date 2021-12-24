
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.07.26
; Description:
; Version: v1.4.2



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns mid-fruits.svg
    (:require [mid-fruits.candy   :refer [param]]
              [mid-fruits.css     :as css]
              [mid-fruits.random  :as random]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- component-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (string) component-id
  ; @param (map)(opt) attributes
  ;
  ; @return (map)
  ;  {:id (string)}
  ([component-id]
   (component-attributes component-id {}))

  ([component-id attributes]
   (component-attributes component-id attributes {}))

  ([component-id attributes additional-attributes]
   (merge {:id component-id} attributes additional-attributes)))

(defn view-box
  ; @param (integer) width
  ; @param (integer) height
  ;
  ; @example
  ;  (svg/view-box 1024 1024)
  ;  =>
  ;  "0 0 1024 1024"
  ;
  ; @return (string)
  [width height]
  (str "0 0 " width " " height))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn linear-gradient
  ; @param (string)(opt) gradient-id
  ; @param (map) gradient-props
  ;  {:x1 (integer)
  ;   :y1 (integer)
  ;   :x2 (integer)
  ;   :y2 (integer)
  ;   :stops (vectors in vector)}
  ;
  ; @usage
  ;  (svg/linear-gradient {...})
  ;
  ;; @usage
  ;  (svg/linear-gradient "my-linear-gradient" {...})
  ;
  ; @example
  ;  (svg/linear-gradient {:x1 0 :y1 0 :x2 100 :y2 0 :stops [[0 "#000"] [100 "#fff"]]})
  ;  =>
  ;  [:linearGradient
  ;    {:x1 "0%" :y1 "0%" :x2 "100%" :y2 "0%"}
  ;    [:stop {:offset "0%"
  ;            :stop-color "#000"}]
  ;    [:stop {:offset "100%"
  ;            :stop-color "#fff"}]]
  ;
  ; @return (hiccup)
  ([gradient-props]
   (let [gradient-id (random/generate-keyword)]
        (linear-gradient gradient-id gradient-props)))

  ([gradient-id {:keys [x1 y1 x2 y2 stops] :as gradient-props}]
   (let [attributes (dissoc gradient-props :x1 :y1 :x2 :y2 :stops)]
        (vec (reduce (fn [linear-gradient stop]
                         (conj linear-gradient [:stop {:offset     (string/percent (first stop))
                                                       :stop-color (second stop)}]))
                     [:linearGradient (component-attributes gradient-id attributes
                                                            {:x1 (string/percent x1)
                                                             :y1 (string/percent y1)
                                                             :x2 (string/percent x2)
                                                             :y2 (string/percent y2)})]
                     (param stops))))))

(defn circle
  ; @param (string)(opt) circle-id
  ; @param (map) circle-props
  ;  {:r (integer)
  ;   :x (integer)
  ;   :y (integer)}
  ;
  ; @usage
  ;  (svg/circle {...})
  ;
  ; @usage
  ;  (svg/circle "my-circle" {...})
  ;
  ; @return (hiccup)
  ([circle-props]
   (let [circle-id (random/generate-keyword)]
        (circle circle-id circle-props)))

  ([circle-id {:keys [x y r] :as circle-props}]
   (let [attributes (dissoc circle-props :r :x :y)]
        [:circle (component-attributes circle-id attributes {:cx x :cy y :r r})])))

(defn polygon
  ; @param (string)(opt) polygon-id
  ; @param (map) polygon-props
  ;  {:points (string)}
  ;
  ; @usage
  ;  (svg/polygon {...})
  ;
  ; @usage
  ;  (svg/polygon "my-polygon" {...})
  ;
  ; @usage
  ;  (svg/polygon {:points "0,100 50,25 50,75 100,0"})
  ;
  ; @return (hiccup)
  ([polygon-props]
   (let [polygon-id (random/generate-keyword)]
        (polygon polygon-id polygon-props)))

  ([polygon-id {:keys [points] :as polygon-props}]
   (let [attributes (dissoc polygon-props :points)]
        [:polygon (component-attributes polygon-id attributes {:points points})])))

(defn svg
  ; @param (string)(opt) svg-id
  ; @param (map) svg-props
  ;  {:elements (vector)
  ;   :height (integer)
  ;   :width (integer)}
  ;
  ; @usage
  ;  (svg/svg {...})
  ;
  ; @usage
  ;  (svg/svg "my-svg" {...})
  ;
  ; @usage
  ;  (svg/svg {:elements [[:polygon ...]] :height 100 :width 100})
  ;
  ; @return (hiccup)
  ([svg-props]
   (let [svg-id (random/generate-keyword)]
        (svg svg-id svg-props)))

  ([svg-id {:keys [elements height width] :as svg-props}]
   (let [attributes (dissoc svg-props :elements :height :width)]
        (vec (reduce #(conj %1 %2)
                      [:svg (component-attributes svg-id attributes
                                                  {:style   {:height "100%" :width "100%"}
                                                   :viewBox (view-box width height)
                                                   :xmlns   "http://www.w3.org/2000/svg"})]
                      (param elements))))))

(defn wrapper
  ; @param (string)(opt) wrapper-id
  ; @param (map) wrapper-props
  ;  {:content (hiccup)
  ;   :height (integer)
  ;   :unit (string)(opt)
  ;    "px", "%"
  ;    Default: "px"
  ;   :width (integer)}
  ;
  ; @usage
  ;  (svg/wrapper {...})
  ;
  ; @usage
  ;  (svg/wrapper "my-wrapper" {...})
  ;
  ; @usage
  ;  (svg/wrapper {:content [:polygon ...] :height 200 :width 100 :unit "px"})
  ;
  ; @return (hiccup)
  ([wrapper-props]
   (let [wrapper-id (random/generate-keyword)]
        (wrapper wrapper-id wrapper-props)))

  ([wrapper-id {:keys [content height width] :as wrapper-props}]
   (let [unit       (get    wrapper-props :unit "px")
         attributes (dissoc wrapper-props :content :height :unit :width)]
        [:div.x-svg--wrapper (component-attributes wrapper-id attributes
                                                   {:style {:height (css/value height unit)
                                                            :width  (css/value width  unit)}})
                             (param content)])))
