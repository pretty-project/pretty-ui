
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns tools.thumbnail-generator.helpers
    (:import  [javax.imageio ImageIO]
              [javax.imageio.plugins.jpeg JPEGImageWriteParam]
              [javax.imageio.stream FileImageOutputStream]
              [javax.imageio IIOImage]
              [javax.imageio ImageWriteParam]
              [java.awt.image BufferedImage]
              [java.awt AlphaComposite]
              [java.awt Image]
              [java.io  File])
    (:require [candy.api :refer [return]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn image-dimensions
  ; @param (java.awt.image.BufferedImage object) image
  ;
  ; @return (integers in vector)
  ;  [(integer) width
  ;   (integer) height]
  [image]
  [(-> image .getWidth)
   (-> image .getHeight)])

(defn scale-dimensions
  ; @param (integers in vector) size
  ; @param (integers in vector) max-size
  ;
  ; @example
  ;  (scale-dimensions [1200 600] [400 150])
  ;  =>
  ;  [300 150]
  ;
  ; @example
  ;  (scale-dimensions [400 150] [1200 600])
  ;  =>
  ;  [1200 450]
  ;
  ; @return (integers in vector)
  ;  [(integer) width
  ;   (integer) height]
  [[width height] [max-width max-height]]
  (let [ratio (max (/ width  max-width)
                   (/ height max-height))]
       [(/ width ratio) (/ height ratio)]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn resize-image
  ; @param (java.awt.image.BufferedImage object) input
  ; @param (map) options
  ;  {:max-height (px)
  ;   :max-width (px)
  ;   :type-int (integer)}
  ;
  ; @return (java.awt.image.BufferedImage object)
  [input {:keys [max-height max-width type-int]}]
  (let [[input-width  input-height]  (image-dimensions input)
        [output-width output-height] (scale-dimensions [input-width input-height] [max-width max-height])
        output    (new BufferedImage output-width output-height type-int)
        graphics  (.createGraphics    output)
        temporary (.getScaledInstance input output-width output-height Image/SCALE_SMOOTH)]
       (.drawImage graphics temporary 0 0 nil)
       (.dispose   graphics)
       (return     output)))
