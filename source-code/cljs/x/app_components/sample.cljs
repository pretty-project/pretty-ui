
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-components.sample
    (:require [x.app-components.api :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Ha a {:content ...} tulajdonságon kívül más beállítás nem kerül átadásra a content
; komponens számára, akkor a {:content ...} tulajdonság rövidített formában is átadható
(defn my-content-a
  []
  [:<> [x.components/content {:content :username}]
       [x.components/content           :username]])

(defn my-content-b
  []
  [:<> [x.components/content {:content "% items uploading ..."     :replacements ["5"]}]
       [x.components/content {:content "%1 downloaded of %2 items" :replacements ["5" "10"]}]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-subs :get-my-component-props (fn [db _] {}))

(defn my-component
  [component-id component-props]
  [:div "My component"])

(defn my-content-c
  []
  [x.components/content :my-component
                        {:content    #'my-component
                         :subscriber [:get-my-component-props]}])

(defn my-content-d
  [x.components/content {:content    [my-component :my-component]
                         :subscriber [:get-my-component-props]}])

; A base-props térkép a my-component komponens component-props paraméterként átadott térképének
; alapját adja (merge)
(defn my-content-e
  [x.components/content {:base-props {:my-key "My value"}
                         :content    [my-component :my-component]
                         :subscriber [:get-my-component-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-subs :get-your-component-props (fn [db _] {}))

(defn your-component
  [component-id component-props]
  [:div "Your component"])

(defn your-stated-a
  []
  [x.components/stated :your-component
                       {:render-f   #'your-component
                        :subscriber [:get-your-component-props]}])

(defn your-stated-b
  []
  [x.components/stated {:component  [your-component :your-component]
                        :subscriber [:get-your-component-props]}])

(defn your-stated-c
  []
  [x.components/stated :your-component
                       {:base-props {}
                        :destructor  [:my-event]
                        :initializer [:your-event]
                        :render-f    #'your-component
                        :subscriber  [:get-your-component-props]}])

; Az initial-props térkép tartalma a komponens React-fába csatolása után elérhető lesz
; a Re-Frame adatbázisban a [:your-path] útvonalon.
(defn your-stated-d
  []
  [x.components/stated :your-component
                       {:initial-props      {}
                        :initial-props-path [:your-path]
                        :render-f           #'your-component
                        :subscriber         [:get-your-component-props]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-subs :get-our-component-props (fn [db _] {}))

(defn our-component
  [component-id component-props])

(defn our-subscriber-a
  []
  [x.components/subscriber :our-component
                           {:render-f   #'our-component
                            :subscriber [:get-our-component-props]}])

(defn our-subscriber-b
  []
  [x.components/subscriber {:component  [our-component :our-component]
                            :subscriber [:get-our-component-props]}])
