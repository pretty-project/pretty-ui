
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.01.20
; Description:
; Version: v1.4.8



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns app-fruits.dom
    (:require [mid-fruits.candy   :refer [param return]]
              [mid-fruits.css     :as css]
              [mid-fruits.io      :as io]
              [mid-fruits.keyword :as keyword]
              [mid-fruits.map     :as map]
              [mid-fruits.math    :as math]
              [mid-fruits.random  :as random]
              [mid-fruits.string  :as string]
              [mid-fruits.vector  :as vector]))



;; -- Configuration -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
;  {:profile-name (map)
;    {:min (px)
;     :max (px)}}
;
; XXX#6408
; - A viewport szélessége alapján kerül kiszámításra
; - A [:meta {:content "width=320" :name "viewport"}] beállítás használatával
;   az eszköz virtual-viewport-width értéke 320 vagy több.
(def VIEWPORT-PROFILES
     {:xxs {:min    0 :max  319}
      :xs  {:min  320 :max  359}
      :s   {:min  360 :max  479}
      :m   {:min  480 :max  719}
      :l   {:min  720 :max 1439}
      :xl  {:min 1440 :max 2159}
      :xxl {:min 2160 :max 9999}})

; @constant (px)
(def SCROLL-DIRECTION-SENSITIVITY 10)



;; -- DOM-event helpers -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-mouse-left
  ; @param (DOM event) mouse-event
  ; @param (function) f
  ;
  ; @return (*)
  [mouse-event f]
  (if (= (.-button mouse-event) 0)
      (f)))

(defn stop-propagation!
  ; @param (DOM event) event
  ; @param (function) f
  ;
  ; @return (nil)
  [event f]
  (.stopPropagation event)
  (f))



;; -- DOM-event converters ----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn event->node-name
  ; @param (DOM event) event
  ;
  ; @return (string)
  [event]
  (.-nodeName (.-srcElement event)))

(defn event->value
  ; @param (dom-event) n
  ;
  ; @return (*)
  [n]
  (-> n .-target .-value))



;; -- Get DOM-element(s) helpers ----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-element-by-id
  ; @param (string) element-id
  ; @param (DOM element)(opt) parent-element
  ;  Default: js/document
  ;
  ; @return (DOM-element or nil)
  [element-id & [parent-element]]
  (.getElementById (or parent-element js/document) element-id))

(defn get-elements-by-query-selector
  ; @param (DOM-element) parent-element
  ; @param (string) query-selector
  ;  XXX#7603
  ;
  ; @usage
  ;  (dom/get-elements-by-query-selector
  ;   head-element link-element "[type=\"text/css\"]")
  ;
  ; @usage
  ;  (dom/get-elements-by-query-selector
  ;   body-element my-element "div.my-class, div.your-class")
  ;
  ; @return (vector)
  [parent-element query-selector]
  (vec (array-seq (.querySelectorAll parent-element query-selector))))

(defn get-elements-by-class-name
  ; @param (string) class-name
  ; @param (DOM element)(opt) parent-element
  ;  Default: js/document
  ;
  ; @return (vector)
  [class-name & [parent-element]]
  (vec (array-seq (.getElementsByClassName (or parent-element js/document) class-name))))

(defn get-elements-by-tag-name
  ; @param (string) tag-name
  ; @param (DOM element)(opt) parent-element
  ;  Default: js/document
  ;
  ; @return (vector)
  [tag-name & [parent-element]]
  (vec (array-seq (.getElementsByTagName (or parent-element js/document) tag-name))))

(defn get-body-element
  ; @return (DOM-element)
  []
  (aget (.getElementsByTagName js/document "body") 0))

(defn get-head-element
  ; @return (DOM-element)
  []
  (aget (.getElementsByTagName js/document "head") 0))

(defn get-document-element
  ; @return (DOM-element)
  []
  (.-documentElement js/document))

(defn get-active-element
  ; @return (DOM-element)
  []
  (.-activeElement js/document))



;; -- Get Document-element data helpers ---------------------------------------
;; ----------------------------------------------------------------------------

(defn get-document-height
  ; @return (integer)
  []
  (.-scrollHeight (.-documentElement js/document)))

(defn get-document-width
  ; @return (integer)
  []
  (.-scrollWidth (.-documentElement js/document)))



;; -- Get viewport data helpers -----------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-viewport-size
  ; @return (map)
  ;  {:viewport.height (integer)
  ;   :viewport.width (integer)}
  []
  {:viewport.height (.-innerHeight js/window)
   :viewport.width  (.-innerWidth  js/window)})

(defn get-viewport-height
  ; @return (integer)
  []
  (.-innerHeight js/window))

(defn get-viewport-width
  ; @return (integer)
  []
  (.-innerWidth js/window))

(defn viewport-profile-match?
  ; @param (keyword) n
  ;
  ; @usage
  ;  (viewport-profile-match? :xl)
  ;
  ; @return (boolean)
  [n]
  (let [viewport-width    (get-viewport-width)
        {:keys [max min]} (n VIEWPORT-PROFILES)]
       (boolean (and (>= viewport-width min)
                     (<= viewport-width max)))))

(defn viewport-profiles-match?
  ; @param (vector) xyz
  ;
  ; @usage
  ;  (viewport-profiles-match? [:xs :s :m])
  ;
  ; @return (boolean)
  [xyz]

  ; Alternative:
  ; (vector/contains-item? xyz (get-viewport-profile))

  (vector/any-item-match? xyz viewport-profile-match?))

(defn get-viewport-profile
  ; @return (keyword)
  []
  (map/get-first-match-key VIEWPORT-PROFILES viewport-profile-match?))

(defn get-viewport-orientation
  ; @return (keyword)
  ;  :landscape, :portrait
  []
  (if (> (get-viewport-height)
         (get-viewport-width))
      (return :portrait)
      (return :landscape)))

(defn landscape-viewport?
  ; @return (boolean)
  []
  (< (get-viewport-height)
     (get-viewport-width)))

(defn portrait-viewport?
  ; @return (boolean)
  []
  (not (landscape-viewport?)))



;; -- Get scroll data helpers -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-scroll-position
  ; @return (map)
  ;  {:scroll.x (integer)
  ;   :scroll-y (integer)}
  []
  (let [document-element (get-document-element)]
       {:scroll.x (.-scrollLeft document-element)
        :scroll.y (.-scrollTop  document-element)}))

(defn get-scroll-x
  ; @return (integer)
  []
  (.-scrollLeft (get-document-element)))

(defn get-scroll-y
  ; @return (integer)
  []
  (.-scrollTop (get-document-element)))

(defn scroll-direction-ttb?
  ; @param (integer) last-scroll-y
  ;
  ; @return (boolean)
  [last-scroll-y]
  (let [scroll-y (get-scroll-y)]
       ; Ha a scroll-y értéke legalább a SCROLL-DIRECTION-SENSITIVITY
       ; értékével nagyobb, mint a last-scroll-y értéke
       (< (+ last-scroll-y SCROLL-DIRECTION-SENSITIVITY)
          (param scroll-y))))

(defn scroll-direction-btt?
  ; @param (integer) last-scroll-y
  ;
  ; @return (boolean)
  [last-scroll-y]
  (let [scroll-y (get-scroll-y)]
       ; Ha a scroll-y értéke legalább a SCROLL-DIRECTION-SENSITIVITY
       ; értékével kisebb, mint a last-scroll-y értéke
       (> (- last-scroll-y SCROLL-DIRECTION-SENSITIVITY)
          (param scroll-y))))

(defn get-scroll-direction
  ; @param (integer) last-scroll-y
  ;
  ; @return (keyword or nil)
  ;   nil, :btt, :ttb
  [last-scroll-y]
  (cond (and (scroll-direction-ttb? last-scroll-y)
             ; XXX#0061
             (math/nonnegative? last-scroll-y))
        (return :ttb)

        (and (scroll-direction-btt? last-scroll-y)
             ; XXX#0061
             (math/nonnegative? last-scroll-y))
        (return :btt)

        ; XXX#0061
        ; Bizonyos böngészőknél, a "scroll bounce effect" az oldal
        ; tetejére nagy lendülettel hirtelen visszagörgetéskor,
        ; a bounce közben – amikor már a scroll-y értéke
        ; a 0 felé közelít negatív irányból – a scroll-direction értékét
        ; :ttb-ként állítaná be, miközben az eredeti gesztus a felfelé görgetés.
        (math/negative? last-scroll-y)
        (return :btt)

        ; XXX#0088
        ; Ha a last-scroll-y és scroll-y értékének különbségének abszolút értéke
        ; nem nagyobb, mint a SCROLL-DIRECTION-SENSITIVITY és nem igaz az XXX#0061
        ; kivétel, akkor a scroll-direction nem megállapítható
        :else nil))

(defn get-scroll-progress
  ; @return (integer)
  ;  0 - 100
  []
  (let [scroll-y        (get-scroll-y)
        document-height (get-document-height)
        viewport-height (get-viewport-height)
        max-scroll-y    (- document-height viewport-height)
        scroll-progress (math/percent max-scroll-y scroll-y)]
      ; A DOM-struktúra felépülése közben előfordul olyan pillanat, amikor
      ; a document-height értéke nem valós, ebből kifolyólag a scroll-progress
      ; értéke ilyenkor kisebb lenne, mint 0.
      (math/between! scroll-progress 0 100)))



;; -- Get mouse data helpers --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-mouse-x
  ; @param (DOM-event) mouse-event
  ;
  ; @return (integer)
  [mouse-event]
  (.-clientX mouse-event))

(defn get-mouse-y
  ; @param (DOM-event) mouse-event
  ;
  ; @return (integer)
  [mouse-event]
  (.-clientY mouse-event))

(defn get-mouse-position
  ; @param (DOM-event) mouse-event
  ;
  ; @return (map)
  ;  {:mouse.x (integer)
  ;   :mouse.y (integer)}
  [mouse-event]
  {:mouse.x (.-clientX mouse-event)
   :mouse.y (.-clientY mouse-event)})



;; -- Get DOM-element data helpers --------------------------------------------
;; ----------------------------------------------------------------------------

(defn element-disabled?
  ; @param (DOM-element) element
  ;
  ; @return (boolean)
  [element]
  (boolean (.-disabled element)))

(defn get-element-size
  ; @param (DOM-element) element
  ;
  ; @return (map)
  ;  {:height (integer)
  ;   :width (integer)}
  [element]
  {:height (.-offsetHeight element)
   :width  (.-offsetWidth  element)})

(defn get-element-width
  ; @param (DOM-element) element
  ;
  ; @return (integer)
  [element]
  (.-offsetWidth element))

(defn get-element-height
  ; @param (DOM-element) element
  ;
  ; @return (integer)
  [element]
  (.-offsetHeight element))

(defn get-element-relative-left
  ; Relative position: relative to viewport position
  ;
  ; @param (DOM-element) element
  ;
  ; @return (integer)
  [element]
  (let [bcr (-> element .getBoundingClientRect)]
       (math/round (.-left bcr))))

(defn get-element-relative-top
  ; Relative position: relative to viewport position
  ;
  ; @param (DOM-element) element
  ;
  ; @return (integer)
  [element]
  (let [bcr (-> element .getBoundingClientRect)]
       (math/round (.-top bcr))))

(defn get-element-absolute-left
  ; Absolute position: relative to document position
  ;
  ; @param (DOM-element) element
  ;
  ; @return (integer)
  [element]
  (math/round (+ (get-element-relative-left element) (get-scroll-x))))

(defn get-element-absolute-top
  ; Absolute position: relative to document position
  ;
  ; @param (DOM-element) element
  ;
  ; @return (integer)
  [element]
  (math/round (+ (get-element-relative-top element) (get-scroll-y))))

(defn get-element-relative-position
  ; Relative position: relative to viewport position
  ;
  ; @param (DOM-element) element
  ;
  ; @return (map)
  ;  {:left (integer)
  ;   :top (integer)}
  [element]
  {:left (get-element-relative-left element)
   :top  (get-element-relative-top  element)})

(defn get-element-absolute-position
  ; Absolute position: relative to document position
  ;
  ; @param (DOM-element) element
  ;
  ; @return (map)
  ;  {:left (integer)
  ;   :top (integer)}
  [element]
  {:left (get-element-absolute-left element)
   :top  (get-element-absolute-top  element)})

(defn get-element-offset-position
  ; Offset position: relative to parent position
  ;
  ; @param (DOM-element) element
  ;
  ; @return (map)
  ;  {:left (integer)
  ;   :top (integer)}
  [element]
  {:left (math/round (.-offsetLeft element))
   :top  (math/round (.-offsetTop  element))})

(defn get-element-computed-style
  ; @param (DOM-element) element
  ;
  ; @return (CSSStyleDeclarationObject)
  ;  The returned object updates automatically when the element's styles are changed
  [element]
  (.getComputedStyle js/window element))

(defn get-element-style-value
  ; @param (DOM-element) element
  ; @param (string) style-name
  ;
  ; @example
  ;  (get-element-style my-element "display")
  ;  => "flex"
  ;
  ; @return (string)
  [element style-name]
  (let [element-computed-style (get-element-computed-style element)]
       (aget element-computed-style style-name)))

(defn get-body-style-value
  ; @param (string) style-name
  ;
  ; @example
  ;  (get-element-style "background")
  ;  => "red"
  ;
  ; @return (string)
  [style-name]
  (let [body-element (get-body-element)]
       (get-element-style-value body-element style-name)))

(defn get-element-masspoint-x
  ; @param (DOM-element) element
  ;
  ; @return (integer)
  [element]
  (let [bcr   (-> element .getBoundingClientRect)
        width (.-offsetWidth element)]
       (math/round (+ (.-left bcr)
                      (/ width 2)
                      (get-scroll-x)))))

(defn get-element-masspoint-y
  ; @param (DOM-element) element
  ;
  ; @return (integer)
  [element]
  (let [bcr    (-> element .getBoundingClientRect)
        height (.-offsetHeight element)]
       (math/round (+ (.-left bcr)
                      (/ height 2)
                      (get-scroll-y)))))

(defn get-element-masspoint-position
  ; @param (DOM-element) element
  ;
  ; @return (map)
  ;  {:x (integer)
  ;   :y (integer)}
  [element]
  {:x (get-element-masspoint-x element)
   :y (get-element-masspoint-y element)})

(defn element-on-viewport-left?
  ; @param (DOM-element) element
  ;
  ; @return (boolean)
  [element]
  (<= (get-element-masspoint-x element)
      (/ (get-viewport-width) 2)))

(defn element-on-viewport-right?
  ; @param (DOM-element) element
  ;
  ; @return (boolean)
  [element]
  (not (element-on-viewport-left? element)))

(defn element-on-viewport-top?
  ; @param (DOM-element) element
  ;
  ; @return (boolean)
  [element]
  (<= (get-element-masspoint-y element)
      (/ (get-viewport-width) 2)))

(defn element-on-viewport-bottom?
  ; @param (DOM-element) element
  ;
  ; @return (boolean)
  [element]
  (not (element-on-viewport-top? element)))

(defn get-element-masspoint-orientation
  ; @param (DOM-element) element
  ;
  ; @return (keyword)
  [element]
  (if (element-on-viewport-bottom? element)
      (if (element-on-viewport-left? element)
          (return :bl)
          (return :br))
      (if (element-on-viewport-left? element)
          (return :tl)
          (return :tr))))



;; -- Focus/blur DOM-element helpers ------------------------------------------
;; ----------------------------------------------------------------------------

(defn focus-element!
  ; @param (DOM-element) element
  [element]
  (.focus element))

(defn blur-element!
  ; @param (DOM-element) element
  [element]
  (.blur element))



;; -- Set DOM-element data helpers --------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-element-id!
  ; @param (DOM-element) element
  ; @param (string) element-id
  ;
  ; @usage
  ;  (dom/set-element-id! my-element "my-element-id")
  [element element-id]
  (set! (.-id element) element-id))

(defn set-element-style!
  ; @param (DOM-element) element
  ; @param (map) style
  ;
  ; @usage
  ;  (dom/set-element-style! my-element {:position "fixed" :top "0"})
  [element style]
  (let [parsed-style (css/parse style)]
       (.setAttribute element "style" parsed-style)))

(defn remove-element-style!
  ; @param (DOM-element) element
  ;
  ; @usage
  ;  (dom/remove-element-style! my-element)
  [element]
  (.removeAttribute element "style"))

(defn set-element-style-value!
  ; @param (DOM-element) element
  ; @param (string) style-name
  ; @param (*) style-value
  [element style-name style-value]
  (aset (.-style element) style-name style-value))

(defn remove-element-style-value!
  ; @param (DOM-element) element
  ; @param (string) style-name
  [element style-name]
  (aset (.-style element) style-name nil))

(defn set-element-class!
  ; @param (DOM-element) element
  ; @param (string) class-name
  [element class-name]
  (.add (.-classList element) class-name))

(defn remove-element-class!
  ; @param (DOM-element) element
  ; @param (string) class-name
  [element class-name]
  (.remove (.-classList element) class-name))



;; -- Set scroll data helpers -------------------------------------------------
;; ----------------------------------------------------------------------------

(defn set-scroll-x!
  ; @param (integer) scroll-x
  ; @param (map)(opt) options
  ;  {:smooth? (boolean)
  ;    Default: false}
  ([scroll-x]
   (set-scroll-x! scroll-x {}))

  ([scroll-x {:keys [smooth?]}]
   ;(set! (.-scrollLeft (get-document-element)) scroll-x)
   (let [scroll-behavior   (if smooth? "smooth" "auto")
         scroll-to-options {"left" scroll-x "top" 0 "behavior" scroll-behavior}]
        (.scrollBy js/window (clj->js scroll-to-options)))))

(defn set-scroll-y!
  ; @param (integer) scroll-y
  ; @param (map)(opt) options
  ;  {:smooth? (boolean)
  ;    Default: false}
  ([scroll-y]
   (set-scroll-y! scroll-y {}))

  ([scroll-y {:keys [smooth?]}]
   ;(set! (.-scrollTop (get-document-element)) scroll-y)

   ; BUG#xxxx
   ; Out of order!
   (let [scroll-behavior   (if smooth? "smooth" "auto")
         scroll-to-options {"left" 0 "top" scroll-y "behavior" scroll-behavior}]
        (.scrollBy js/window (clj->js scroll-to-options)))
        
   (set! (.-scrollTop (get-document-element)) scroll-y)))

(defn scroll-to-element-top!
  ; @param (DOM-element) element
  ; @param (px)(opt) offset
  ([element]
   (scroll-to-element-top! element 0))

  ([element offset]
   (set-scroll-y! (+ (get-element-absolute-top element) offset))))



;; -- Set DOM-element attribute helpers ---------------------------------------
;; ----------------------------------------------------------------------------

(defn set-element-attribute!
  ; @param (DOM-element) element
  ; @param (string) attribute-name
  ; @param (string) attribute-value
  ;
  ; @return (undefined)
  [element attribute-name attribute-value]
  (.setAttribute element attribute-name attribute-value))

(defn remove-element-attribute!
  ; @param (DOM-element) element
  ; @param (string) attribute-name
  ;
  ; @return (undefined)
  [element attribute-name]
  (.removeAttribute element attribute-name))



;; -- DOM-node helpers --------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn insert-before!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ; @param (DOM-element) after-element
  [parent-element child-element after-element]
  (.insertBefore parent-element child-element after-element))

(defn insert-after!
  ; https://www.javascripttutorial.net/javascript-dom/javascript-insertafter/
  ;
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ; @param (DOM-element) before-element
  [parent-element child-element before-element]
  (let [next-sibling-element (.-nextSibling before-element)]
       (insert-before! parent-element child-element next-sibling-element)))

(defn insert-as-first-of-type!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  [parent-element child-element]
  (let [tag-name      (.-tagName child-element)
        all-of-type   (get-elements-by-tag-name tag-name parent-element)
        first-of-type (first all-of-type)]
       (insert-before! parent-element child-element first-of-type)))

(defn insert-as-last-of-type!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  [parent-element child-element]
  (let [tag-name     (.-tagName child-element)
        all-of-type  (get-elements-by-tag-name tag-name parent-element)
        last-of-type (last all-of-type)]
       (insert-after! parent-element child-element last-of-type)))

(defn insert-as-first-of-query-selected!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ; @param (string) query-selector
  ;  XXX#7603
  ;
  ; @usage
  ;  (dom/insert-as-first-of-query-selected!
  ;   head-element link-element "[type=\"text/css\"]")
  ;
  ; @usage
  ;  (dom/insert-as-first-of-query-selected!
  ;   body-element my-element "div.my-class, div.your-class")
  [parent-element child-element query-selector]
  (let [all-of-query-selected   (get-elements-by-query-selector parent-element query-selector)
        first-of-query-selected (first all-of-query-selected)]
       (insert-before! parent-element child-element first-of-query-selected)))

(defn insert-as-last-of-query-selected!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  ; @param (string) query-selector
  ;  XXX#7603
  ;
  ; @usage
  ;  (dom/insert-as-first-of-query-selected!
  ;   head-element link-element "[type=\"text/css\"]")
  ;
  ; @usage
  ;  (dom/insert-as-first-of-query-selected!
  ;   body-element my-element "div.my-class, div.your-class")
  [parent-element child-element query-selector]
  (let [all-of-query-selected  (get-elements-by-query-selector parent-element query-selector)
        last-of-query-selected (last all-of-query-selected)]
       (insert-after! parent-element child-element last-of-query-selected)))

(defn append-element!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  [parent-element child-element]
  (.appendChild parent-element child-element))

(defn prepend-element!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  [parent-element child-element]
  (insert-before! parent-element child-element (.-firstChild parent-element)))

(defn create-element!
  ; @param (string) nodename
  ;
  ; @usage
  ;  (dom/create-element! "div")
  ;
  ; @return (DOM-element)
  [nodename]
  (.createElement js/document nodename))

(defn empty-element!
  ; @param (DOM-element) element
  [element]
  (while (.-firstChild element)
         (.removeChild element (.-firstChild element))))

(defn remove-child!
  ; @param (DOM-element) parent-element
  ; @param (DOM-element) child-element
  [parent-element child-element]
  (.removeChild parent-element child-element))

(defn remove-element!
  ; @param (DOM-element) element
  [element]
  (.remove element))

(defn set-element-content!
  ; @param (DOM-element) element
  ; @param (string) content
  ;
  ; @usage
  ;  (dom/set-element-content! element "Hakuna Matata!")
  ;
  ; @return (string)
  [element content]
  (set! (.-innerHTML element) content))



;; -- Event-listener helpers --------------------------------------------------
;; ----------------------------------------------------------------------------

(defn add-event-listener!
  ; @param (string) type
  ; @param (function) listener
  ; @param (DOM-element)(opt) target
  ;  Default: js/window
  [type listener & [target]]
  (.addEventListener (or target js/window) type listener false))

(defn remove-event-listener!
  ; @param (string) type
  ; @param (function) listener
  ; @param (DOM-element)(opt) target
  ;  Default: js/window
  [type listener & [target]]
  (.removeEventListener (or target js/window) type listener false))



;; -- Mouse helpers -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-preventer
  ; @param (DOM-event) event
  [event]
  (let [node-name (string/lowercase (event->node-name event))]
       ; Az input es textarea elemek hasznalatahoz szukseg van mouse-down eventre!
       (if-not (vector/contains-item? ["input" "textarea"] node-name)
               (let [active-element (get-active-element)]
                    (.preventDefault event)
                    (.blur active-element)))))



;; -- Touch helpers -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn touch-events-api-detected?
  ; @return (boolean)
  []
  (boolean (or (.hasOwnProperty js/window "ontouchstart")
               (> (.-maxTouchPoints   (.-navigator js/window)) 0)
               (> (.-msMaxTouchPoints (.-navigator js/window)) 0)
               (and (.-DocumentTouch js/window)
                    (instance? "DocumentTouch" js/document)))))



;; -- File converters ---------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file->filesize
  ; @param (file object) file
  ;
  ; @return (B)
  [file]
  (.-size file))

(defn file->filename
  ; @param (file object) file
  ;
  ; @return (B)
  [file]
  (.-name file))

(defn file->mime-type
  ; @param (file object) file
  ;
  ; @return (string)
  [file]
  (.-type file))

(defn file->image?
  ; @param (file object) file
  ;
  ; @return (boolean)
  [file]
  (let [mime-type (file->mime-type file)]
       (io/mime-type->image? mime-type)))

(defn file->file-data
  ; @param (file object) file
  ;
  ; @return (map)
  ;  {:filename (string)
  ;   :filesize (B)
  ;   :mime-type (string)}
  [file]
  {:filename  (file->filename  file)
   :filesize  (file->filesize  file)
   :mime-type (file->mime-type file)})



;; -- Form-data functions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn append-to-form-data!
  ; @param (FormData object)
  ; @param (keyword or string) prop-id
  ; @param (*) prop-value
  ;
  ; @usage
  ;  (def my-form-data (js/FormData.))
  ;  (dom/append-to-form-data! my-form-data :name "John")
  ;
  ; @return (FormData object)
  [form-data prop-id prop-value]
  (let [prop-id (if (keyword? prop-id)
                    (name     prop-id)
                    (return   prop-id))]
       (.append form-data prop-id prop-value)
       (return  form-data)))

(defn merge-to-form-data!
  ; @param (FormData object) form-data
  ; @param (list of maps) xyz
  ;
  ; @usage
  ;  (def my-form-data (js/FormData.))
  ;  (dom/merge-to-form-data! my-form-data {...})
  ;
  ; @usage
  ;  (def my-form-data (js/FormData.))
  ;  (dom/merge-to-form-data! my-form-data {...} {...} {...})
  ;
  ; @return (FormData object)
  [form-data & xyz]
  (doseq [n xyz]
         (doseq [[k v] n]
                (append-to-form-data! form-data k v)))
  (return form-data))



;; -- File selector converters ------------------------------------------------
;; ----------------------------------------------------------------------------

(defn file-selector->files
  ; @param (DOM-element) file-selector
  ;
  ; @return (?)
  [file-selector]
  (.-files file-selector))

(defn file-selector->file-list
  ; @param (DOM-element) file-selector
  ;
  ; @return (?)
  [file-selector]
  (let [files (file-selector->files file-selector)]
       (array-seq files)))

(defn file-selector->file
  ; @param (DOM-element) file-selector
  ; @param (integer) file-dex
  ;
  ; @return (file object)
  [file-selector file-dex]
  (let [file-list (file-selector->file-list file-selector)]
       (nth file-list file-dex)))

(defn file-selector->files-size
  ; @param (DOM-element) file-selector
  ;
  ; @return (float)
  [file-selector]
  (reduce #(+ %1 (file->filesize %2))
           (param 0)
           (file-selector->file-list file-selector)))

(defn file-selector->files-count
  ; @param (DOM-element) file-selector
  ;
  ; @return (integer)
  [file-selector]
  (.-length (.-files file-selector)))

(defn file-selector->any-file-selected?
  ; @param (DOM-element) file-selector
  ;
  ; @return (boolean)
  [file-selector]
  (let [files-count (file-selector->files-count file-selector)]
       (> files-count 0)))

(defn file-selector->mime-types
  ; @param (DOM-element) file-selector
  ;
  ; @return (strings in vector)
  [file-selector]
  (reduce #(vector/conj-item %1 (file->mime-type %2))
           (param [])
           (file-selector->file-list file-selector)))

(defn file-selector->files-data
  ; @param (DOM-element) file-selector
  ;
  ; @return (maps in vector)
  [file-selector]
  (reduce #(vector/conj-item %1 (file->file-data %2))
           (param [])
           (file-selector->file-list file-selector)))

(defn file-selector->file-selector-data
  ; @param (DOM-element) file-selector
  ;
  ; @return (map)
  ;  {:any-file-selected? (boolean)
  ;   :files-count (integer)
  ;   :files-data (maps in vector)
  ;   :files-size (B)}
  [file-selector]
  {:any-file-selected? (file-selector->any-file-selected? file-selector)
   :files-count        (file-selector->files-count        file-selector)
   :files-data         (file-selector->files-data         file-selector)
   :files-size         (file-selector->files-size         file-selector)})

(defn file-selector->file-object-url
  ; @param (DOM-element) file-selector
  ; @param (integer) file-dex
  ;
  ; @return (string)
  [file-selector file-dex]
  (let [file (file-selector->file file-selector file-dex)]
       (.createObjectURL js/URL file)))

(defn file-selector->image-data-url
  ; @param (DOM-element) file-selector
  ; @param (integer) file-dex
  ;
  ; @return (string)
  [file-selector file-dex]
  (let [file        (file-selector->file file-selector file-dex)
        file-reader (js/FileReader.)]))

(defn file-selector->form-data
  ; @param (DOM-element) file-selector
  ; @param (strings in vector)(opt) filtered-file-keys
  ;
  ; @usage
  ;  (def my-file-selector (dom/get-elementy-by-id "my-file-selector"))
  ;  (dom/file-selector->form-data my-file-selector)
  ;
  ; @usage
  ;  (def my-file-selector (dom/get-elementy-by-id "my-file-selector"))
  ;  (dom/file-selector->form-data my-file-selector ["0" "1" "4"])
  ;
  ; @return (FormData object)
  [file-selector & [filtered-file-keys]]
  (let [files     (file-selector->files file-selector)
        form-data (js/FormData.)
        file-keys (.keys js/Object files)
        file-keys (or filtered-file-keys file-keys)]
       (doseq [file-key file-keys]
              (let [file (aget files file-key)]
                   (append-to-form-data! form-data file-key file)))
       (return form-data)))



;; -- Selection functions -----------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-selection-start
  ; @param (DOM-element) element
  ;
  ; @return (integer)
  [element]
  (.-selectionStart element))

(defn get-selection-end
  ; @param (DOM-element) element
  ;
  ; @return (integer)
  [element]
  (.-selectionStart element))

(defn get-selection-range
  ; @param (DOM-element) element
  ;
  ; @return (map)
  ;  {:start (integer)
  ;   :end (integer)}
  [element]
  {:start (get-selection-start element)
   :end   (get-selection-end   element)})

(defn set-selection-start!
  ; @param (DOM-element)
  ; @param (integer) selection-start
  ;
  ; @return (?)
  [element selection-start]
  (set! (.-selectionStart element)
        (param selection-start)))

(defn set-selection-end!
  ; @param (DOM-element)
  ; @param (integer) selection-end
  ;
  ; @return (?)
  [element selection-end]
  (set! (.-selectionEnd element)
        (param selection-end)))

(defn set-selection-range!
  ; @param (DOM-element) element
  ; @param (map) selection-range
  ;  {:start (integer)
  ;   :end (integer)}
  ;
  ; @return (?)
  [element {:keys [start end]}]
  (set-selection-start! element start)
  (set-selection-end!   element end))



;; -- Converters --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn px->vh
  ; @param (integer) n
  ;
  ; @return (integer)
  [n]
  (let [vh (/ (get-viewport-height) 100)]
       (math/floor (/ n vh))))

(defn px->vw
  ; @param (integer) n
  ;
  ; @return (integer)
  [n]
  (let [vh (/ (get-viewport-width) 100)]
       (math/floor (/ n vh))))

(defn vh->px
  ; @param (integer) n
  ;
  ; @return (integer)
  [n]
  (let [vh (/ (get-viewport-height) 100)]
       (math/floor (* n vh))))

(defn vw->px
  ; @param (integer) n
  ;
  ; @return (integer)
  [n]
  (let [vh (/ (get-viewport-height) 100)]
       (math/floor (* n vh))))
